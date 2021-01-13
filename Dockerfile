FROM maven:3.6.3-jdk-11

USER root

# install google chrome
RUN apt-get update 
RUN apt-get install -y gconf-service libasound2 libatk1.0-0 libcairo2 libcups2 libfontconfig1 libgdk-pixbuf2.0-0 libgtk-3-0 libnspr4 libpango-1.0-0 libxss1 fonts-liberation libappindicator1 libnss3 lsb-release xdg-utils
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
RUN apt-get -y update
RUN apt-get install -y google-chrome-stable

#install firefox
RUN mkdir -p opt/firefox_headless && cd opt/firefox_headless && wget https://ftp.mozilla.org/pub/firefox/releases/57.0/linux-x86_64/en-US/firefox-57.0.tar.bz2 && tar -xjf firefox-57.0.tar.bz2 && rm firefox-57.0.tar.bz2
ENV FIREFOX_BIN=opt/firefox_headless/firefox/firefox

ARG WORKSPACE="/opt/workspace/core"

RUN mkdir -p ${WORKSPACE}
    
ADD . ${WORKSPACE}

WORKDIR ${WORKSPACE}

RUN chmod +x entrypoint.sh
RUN cp entrypoint.sh /entrypoint.sh

RUN mvn -B -s settings.xml verify
RUN mvn -B -s settings.xml install

RUN rm settings.xml

WORKDIR /

CMD ["bash", "/entrypoint.sh"]
