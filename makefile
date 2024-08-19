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

count:
	@echo "Total valid lines of code:"
	@find src/main/scala/npc -name "*.scala" | xargs cat | grep -v '^\s*//' | grep -v '^\s*\*\|/\*\|\*/' | wc -l
