PRG = top
BUILD = generated

sim:
	sbt "runMain npc.$(PRG)"
	python3 clean.py

clean:
	rm -rf $(BUILD)

git:
	git add .
	git commit
