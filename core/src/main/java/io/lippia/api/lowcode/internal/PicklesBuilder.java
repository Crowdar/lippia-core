package io.lippia.api.lowcode.internal;

import cucumber.runtime.model.CucumberFeature;

import gherkin.events.PickleEvent;
import gherkin.pickles.PickleLocation;
import gherkin.pickles.PickleTag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.lippia.api.lowcode.patterns.Patterns.LOCATION_EXCLUSION_PATTERN;
import static io.lippia.api.lowcode.patterns.Patterns.LOCATION_INCLUSION_PATTERN;
import static io.lippia.api.lowcode.patterns.Patterns.SCENARIO_NAME_EXCLUSION_PATTERN;
import static io.lippia.api.lowcode.patterns.Patterns.SCENARIO_NAME_INCLUSION_PATTERN;
import static io.lippia.api.lowcode.patterns.Patterns.TAG_EXCLUSION_PATTERN;
import static io.lippia.api.lowcode.patterns.Patterns.TAG_INCLUSION_PATTERN;

public class PicklesBuilder {

    public static class Builder {
        private String featureName;
        private String separatorType;
        private String filterBy;

        public Builder from(String featureName) {
            this.featureName = featureName;
            return this;
        }

        public Builder separatorType(String separatorType) {
            this.separatorType = separatorType;
            return this;
        }

        public Builder filterBy(String filterBy) {
            this.filterBy = filterBy;
            return this;
        }

        public List<PickleEvent> build() {
            String expression = separatorType.concat(filterBy);
            List<CucumberFeature> features = CucumberInternal.getCucumberFeatures(featureName);
            for (CucumberFeature feature: features) {
                List<PickleEvent> pickleEvents = feature.getPickles();
                if (expression.matches(LOCATION_INCLUSION_PATTERN.pattern())) {
                    return getEventsFromIncludedLocationsIn(pickleEvents);
                } else if (expression.matches(LOCATION_EXCLUSION_PATTERN.pattern())) {
                    return getEventsFromExcludedLocationsIn(pickleEvents);
                } else if (expression.matches(TAG_INCLUSION_PATTERN.pattern())) {
                    return getEventsFromIncludedTagsIn(pickleEvents);
                } else if (expression.matches(TAG_EXCLUSION_PATTERN.pattern())) {
                    return getEventsFromExcludedTagsIn(pickleEvents);
                } else if (expression.matches(SCENARIO_NAME_INCLUSION_PATTERN.pattern())) {
                    return getEventsFromIncludedScenariosNameIn(pickleEvents);
                } else if (expression.matches(SCENARIO_NAME_EXCLUSION_PATTERN.pattern())) {
                    return getEventsFromExcludedScenariosNameIn(pickleEvents);
                }
            }

            return new ArrayList<>();
        }

        public List<PickleEvent> getEventsFromIncludedTagsIn(List<PickleEvent> pickleEvents) {
            try {
                return pickleEvents.stream().filter(this::tagMatches).collect(Collectors.toList());
            } catch (TypeNotPresentException e) {
                throw new RuntimeException("You must specify an existent tag");
            }
        }

        public List<PickleEvent> getEventsFromExcludedTagsIn(List<PickleEvent> pickleEvents) {
            this.filterBy = filterBy.substring(1);
            try {
                return pickleEvents.stream().filter(this::tagDoesNotMatches).collect(Collectors.toList());
            } catch (TypeNotPresentException e) {
                throw new RuntimeException("You must specify an existent tag");
            }
        }

        public List<PickleEvent> getEventsFromIncludedLocationsIn(List<PickleEvent> pickleEvents) {
            try {
                return pickleEvents.stream().filter(this::locationMatches).collect(Collectors.toList());
            } catch (TypeNotPresentException e) {
                throw new RuntimeException("You must specify an existent location");
            }
        }

        public List<PickleEvent> getEventsFromExcludedLocationsIn(List<PickleEvent> pickleEvents) {
            this.filterBy = filterBy.substring(1);
            try {
                return pickleEvents.stream().filter(this::locationDoesNotMatches).collect(Collectors.toList());
            } catch (TypeNotPresentException e) {
                throw new RuntimeException("You must specify an existent location");
            }
        }

        public List<PickleEvent> getEventsFromIncludedScenariosNameIn(List<PickleEvent> pickleEvents) {
            try {
                return pickleEvents.stream().filter(this::scenarioNameMatches).collect(Collectors.toList());
            } catch (TypeNotPresentException e) {
                throw new RuntimeException("You must specify an existent scenario name");
            }
        }

        public List<PickleEvent> getEventsFromExcludedScenariosNameIn(List<PickleEvent> pickleEvents) {
            this.filterBy = filterBy.substring(1);
            try {
                return pickleEvents.stream().filter(this::scenarioDoesNotMatches).collect(Collectors.toList());
            } catch (TypeNotPresentException e) {
                throw new RuntimeException("You must specify an existent scenario name");
            }
        }

        private boolean locationMatches(PickleEvent event) {
            return event.pickle.getLocations().stream().anyMatch(this::locationMatches);
        }

        private boolean locationDoesNotMatches(PickleEvent event) {
            return event.pickle.getLocations().stream().noneMatch(this::locationMatches);
        }

        private boolean tagMatches(PickleEvent event) {
            return event.pickle.getTags().stream().anyMatch(this::tagMatches);
        }

        private boolean tagDoesNotMatches(PickleEvent event) {
            return event.pickle.getTags().stream().noneMatch(this::tagMatches);
        }

        private boolean tagMatches(PickleTag currentTag) {
            return currentTag.getName().equals(separatorType.concat(filterBy));
        }

        private boolean locationMatches(PickleLocation currentLocation) {
            return currentLocation.getLine() == Integer.parseInt(filterBy);
        }

        private boolean scenarioNameMatches(PickleEvent event) {
            return event.pickle.getName().equals(filterBy);
        }

        private boolean scenarioDoesNotMatches(PickleEvent event) {
            return !event.pickle.getName().equals(filterBy);
        }

    }

}