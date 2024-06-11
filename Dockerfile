FROM ubuntu:latest
RUN apt-get update && apt-get install -y default-jre

RUN mkdir /server
WORKDIR /server

COPY app/build/distributions/app.tar ./
RUN tar xf app.tar --directory ./
RUN rm app.tar

CMD ["sh", "app/bin/app"]
EXPOSE 2020
