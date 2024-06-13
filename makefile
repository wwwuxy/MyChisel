PRG = Main
BUILD = generated

sim:
	sbt "runMain npc.$(PRG)"

clean:
	rm -rf $(BUILD)
