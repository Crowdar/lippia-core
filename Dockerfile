FROM maven:3.6.3-jdk-11

USER root

# install google chrome
RUN apt-get update 
# RUN apt-get install -y gconf-service libasound2 libatk1.0-0 libcairo2 libcups2 libfontconfig1 libgdk-pixbuf2.0-0 libgtk-3-0 libnspr4 libpango-1.0-0 libxss1 fonts-liberation libappindicator1 libnss3 lsb-release xdg-utils
# RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add -
# RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
# RUN apt-get update
# RUN apt-get install -y google-chrome-stable

RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN apt-get install -y libsystemd0 libudev1 
RUN apt install -y ./google-chrome-stable_current_amd64.deb

ARG WORKSPACE="/opt/workspace/core"

RUN mkdir -p ${WORKSPACE}
    
ADD . ${WORKSPACE}

WORKDIR ${WORKSPACE}

RUN chmod +x entrypoint.sh
RUN cp entrypoint.sh /entrypoint.sh

RUN curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose

RUN mvn -B verify
RUN mvn -B install
RUN mvn versions:update-parent

WORKDIR /

CMD ["bash", "/entrypoint.sh"]
