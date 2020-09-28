export PATH := bin:$(PATH)

.DEFAULT_GOAL := alv

alv:
	ant -Dnb.internal.action.name=rebuild clean jar

clean:
	ant -Dnb.internal.action.name=clean clean

exec:
	./run_production_plan_APP.sh

r:
	./run_production_plan_APP.sh

b:
	ant -Dnb.internal.action.name=rebuild clean jar

check:
	ant -Dnb.internal.action.name=test

javadoc:
	ant -Dnb.internal.action.name=javadoc
