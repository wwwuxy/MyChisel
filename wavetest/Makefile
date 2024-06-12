TOPNAME = top
# INC_PATH ?=

# # rules for verilator
# INCFLAGS = $(addprefix -I, $(INC_PATH))
# CXXFLAGS += $(INCFLAGS) -DTOP_NAME="\"V$(TOPNAME)\""

# project source
VSRCS = $(shell find $(abspath ./vsrc) -name "*.v")
CSRCS = $(shell find $(abspath ./csrc) -name "*.c" -or -name "*.cc" -or -name "*.cpp")

all:
	sim	clean run
	@echo "Write this Makefile by your self."

sim:
	verilator --cc --exe --build --threads-dpi all	\
	-j 16 -Wall --trace --top-module $(TOPNAME)	\
	$(CSRCS) $(VSRCS)



clean:
	rm -rf obj_dir *.vcd

# include ../Makefile