PRG = Elaborate
BUILD = generated

sim:
	sbt "runMain npc.$(PRG)"
	python3 clean.py
	mv generated/ysyx_23060192.v $(NPC_HOME)/vsrc/ysyx_23060192.v

clean:
	rm -rf $(BUILD)

git:
	git add .
	git commit
push:
	git push origin Soc
