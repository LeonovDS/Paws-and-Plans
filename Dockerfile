FROM ubuntu:latest
RUN apt-get update && apt-get install -y default-jre

RUN mkdir /server
WORKDIR /server

COPY app/build/distributions/app.tar ./
RUN tar xf app.tar --directory ./
RUN rm app.tar

RUN mkdir frontend
COPY frontend ./frontend

CMD ["sh", "app/bin/app"]
EXPOSE 8080