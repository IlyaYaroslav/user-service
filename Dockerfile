FROM eclipse/
COPY /target..

ENTRYPOINT["-java"]