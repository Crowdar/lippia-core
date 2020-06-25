FROM maven:3.6.3-jdk-11

USER root

# install google chrome
RUN apt-get update 
RUN apt-get install -y gconf-service libasound2 libatk1.0-0 libcairo2 libcups2 libfontconfig1 libgdk-pixbuf2.0-0 libgtk-3-0 libnspr4 libpango-1.0-0 libxss1 fonts-liberation libappindicator1 libnss3 lsb-release xdg-utils
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
RUN apt-get -y update
RUN apt-get install -y google-chrome-stable

ARG WORKSPACE="/opt/workspace/core"

RUN mkdir -p ${WORKSPACE}
    
ADD . ${WORKSPACE}

WORKDIR ${WORKSPACE}

RUN mvn -B -s settings.xml verify
RUN mvn -B -s settings.xml install 

RUN rm settings.xml

WORKDIR /

ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
CMD ["mvn"]