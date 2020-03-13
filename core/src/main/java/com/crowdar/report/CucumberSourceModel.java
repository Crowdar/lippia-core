package com.crowdar.report;

import java.util.HashMap;
import java.util.Map;

import cucumber.api.event.TestSourceRead;
import gherkin.AstBuilder;
import gherkin.GherkinDialect;
import gherkin.GherkinDialectProvider;
import gherkin.Parser;
import gherkin.ParserException;
import gherkin.TokenMatcher;
import gherkin.ast.Background;
import gherkin.ast.Examples;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.Node;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Step;
import gherkin.ast.TableRow;

public class CucumberSourceModel {
    private final Map<String, TestSourceRead> pathToReadEventMap = new HashMap<String, TestSourceRead>();
    private final Map<String, GherkinDocument> pathToAstMap = new HashMap<String, GherkinDocument>();
    private final Map<String, Map<Integer, CucumberSourceModel.AstNode>> pathToNodeMap = new HashMap<String, Map<Integer, CucumberSourceModel.AstNode>>();

    static Feature getFeatureForTestCase(CucumberSourceModel.AstNode astNode) {
        while (astNode.parent != null) {
            astNode = astNode.parent;
        }
        return (Feature) astNode.node;
    }

    static Background getBackgroundForTestCase(CucumberSourceModel.AstNode astNode) {
        Feature feature = getFeatureForTestCase(astNode);
        ScenarioDefinition backgound = feature.getChildren().get(0);
        if (backgound instanceof Background) {
            return (Background) backgound;
        } else {
            return null;
        }
    }

    static ScenarioDefinition getScenarioDefinition(CucumberSourceModel.AstNode astNode) {
        return astNode.node instanceof ScenarioDefinition ? (ScenarioDefinition) astNode.node : (ScenarioDefinition) astNode.parent.parent.node;
    }

    static boolean isScenarioOutlineScenario(CucumberSourceModel.AstNode astNode) {
        return !(astNode.node instanceof ScenarioDefinition);
    }

    static boolean isBackgroundStep(CucumberSourceModel.AstNode astNode) {
        return astNode.parent.node instanceof Background;
    }

    static String calculateId(CucumberSourceModel.AstNode astNode) {
        Node node = astNode.node;
        if (node instanceof ScenarioDefinition) {
            return calculateId(astNode.parent) + ";" + convertToId(((ScenarioDefinition) node).getName());
        }
        if (node instanceof CucumberSourceModel.ExamplesRowWrapperNode) {
            return calculateId(astNode.parent) + ";" + Integer.toString(((CucumberSourceModel.ExamplesRowWrapperNode) node).bodyRowIndex + 2);
        }
        if (node instanceof TableRow) {
            return calculateId(astNode.parent) + ";" + Integer.toString(1);
        }
        if (node instanceof Examples) {
            return calculateId(astNode.parent) + ";" + convertToId(((Examples) node).getName());
        }
        if (node instanceof Feature) {
            return convertToId(((Feature) node).getName());
        }
        return "";
    }

    static String convertToId(String name) {
        return name.replaceAll("[\\s'_,!]", "-").toLowerCase();
    }

    void addTestSourceReadEvent(String path, TestSourceRead event) {
        pathToReadEventMap.put(path, event);
    }

    Feature getFeature(String path) {
        if (!pathToAstMap.containsKey(path)) {
            parseGherkinSource(path);
        }
        if (pathToAstMap.containsKey(path)) {
            return pathToAstMap.get(path).getFeature();
        }
        return null;
    }

    ScenarioDefinition getScenarioDefinition(String path, int line) {
        return getScenarioDefinition(getAstNode(path, line));
    }

    CucumberSourceModel.AstNode getAstNode(String path, int line) {
        if (!pathToNodeMap.containsKey(path)) {
            parseGherkinSource(path);
        }
        if (pathToNodeMap.containsKey(path)) {
            return pathToNodeMap.get(path).get(line);
        }
        return null;
    }

    boolean hasBackground(String path, int line) {
        if (!pathToNodeMap.containsKey(path)) {
            parseGherkinSource(path);
        }
        if (pathToNodeMap.containsKey(path)) {
            CucumberSourceModel.AstNode astNode = pathToNodeMap.get(path).get(line);
            return getBackgroundForTestCase(astNode) != null;
        }
        return false;
    }

    String getKeywordFromSource(String uri, int stepLine) {
        Feature feature = getFeature(uri);
        if (feature != null) {
            TestSourceRead event = getTestSourceReadEvent(uri);
            String trimmedSourceLine = event.source.split("\n")[stepLine - 1].trim();
            GherkinDialect dialect = new GherkinDialectProvider(feature.getLanguage()).getDefaultDialect();
            for (String keyword : dialect.getStepKeywords()) {
                if (trimmedSourceLine.startsWith(keyword)) {
                    return keyword;
                }
            }
        }
        return "";
    }

    private TestSourceRead getTestSourceReadEvent(String uri) {
        if (pathToReadEventMap.containsKey(uri)) {
            return pathToReadEventMap.get(uri);
        }
        return null;
    }

    String getFeatureName(String uri) {
        Feature feature = getFeature(uri);
        if (feature != null) {
            return feature.getName();
        }
        return "";
    }

    private void parseGherkinSource(String path) {
        if (!pathToReadEventMap.containsKey(path)) {
            return;
        }
        Parser<GherkinDocument> parser = new Parser<GherkinDocument>(new AstBuilder());
        TokenMatcher matcher = new TokenMatcher();
        try {
            GherkinDocument gherkinDocument = parser.parse(pathToReadEventMap.get(path).source, matcher);
            pathToAstMap.put(path, gherkinDocument);
            Map<Integer, CucumberSourceModel.AstNode> nodeMap = new HashMap<Integer, CucumberSourceModel.AstNode>();
            CucumberSourceModel.AstNode currentParent = new CucumberSourceModel.AstNode(gherkinDocument.getFeature(), null);
            for (ScenarioDefinition child : gherkinDocument.getFeature().getChildren()) {
                processScenarioDefinition(nodeMap, child, currentParent);
            }
            pathToNodeMap.put(path, nodeMap);
        } catch (ParserException e) {
            // Ignore exceptions
        }
    }

    private void processScenarioDefinition(Map<Integer, CucumberSourceModel.AstNode> nodeMap, ScenarioDefinition child, CucumberSourceModel.AstNode currentParent) {
        CucumberSourceModel.AstNode childNode = new CucumberSourceModel.AstNode(child, currentParent);
        nodeMap.put(child.getLocation().getLine(), childNode);
        for (Step step : child.getSteps()) {
            nodeMap.put(step.getLocation().getLine(), new CucumberSourceModel.AstNode(step, childNode));
        }
        if (child instanceof ScenarioOutline) {
            processScenarioOutlineExamples(nodeMap, (ScenarioOutline) child, childNode);
        }
    }

    private void processScenarioOutlineExamples(Map<Integer, CucumberSourceModel.AstNode> nodeMap, ScenarioOutline scenarioOutline, CucumberSourceModel.AstNode childNode) {
        for (Examples examples : scenarioOutline.getExamples()) {
            CucumberSourceModel.AstNode examplesNode = new CucumberSourceModel.AstNode(examples, childNode);
            TableRow headerRow = examples.getTableHeader();
            CucumberSourceModel.AstNode headerNode = new CucumberSourceModel.AstNode(headerRow, examplesNode);
            nodeMap.put(headerRow.getLocation().getLine(), headerNode);
            for (int i = 0; i < examples.getTableBody().size(); ++i) {
                TableRow examplesRow = examples.getTableBody().get(i);
                Node rowNode = new CucumberSourceModel.ExamplesRowWrapperNode(examplesRow, i);
                CucumberSourceModel.AstNode expandedScenarioNode = new CucumberSourceModel.AstNode(rowNode, examplesNode);
                nodeMap.put(examplesRow.getLocation().getLine(), expandedScenarioNode);
            }
        }
    }

    class ExamplesRowWrapperNode extends Node {
        final int bodyRowIndex;

        ExamplesRowWrapperNode(Node examplesRow, int bodyRowIndex) {
            super(examplesRow.getLocation());
            this.bodyRowIndex = bodyRowIndex;
        }
    }

    class AstNode {
        final Node node;
        final CucumberSourceModel.AstNode parent;

        AstNode(Node node, CucumberSourceModel.AstNode parent) {
            this.node = node;
            this.parent = parent;
        }
    }
}
