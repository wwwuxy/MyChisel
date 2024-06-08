// Generated by CIRCT firtool-1.62.0
// Standard header to adapt well known macros for register randomization.
`ifndef RANDOMIZE
  `ifdef RANDOMIZE_REG_INIT
    `define RANDOMIZE
  `endif // RANDOMIZE_REG_INIT
`endif // not def RANDOMIZE

// RANDOM may be set to an expression that produces a 32-bit random unsigned value.
`ifndef RANDOM
  `define RANDOM $random
`endif // not def RANDOM

// Users can define INIT_RANDOM as general code that gets injected into the
// initializer block for modules with registers.
`ifndef INIT_RANDOM
  `define INIT_RANDOM
`endif // not def INIT_RANDOM

// If using random initialization, you can also define RANDOMIZE_DELAY to
// customize the delay used, otherwise 0.002 is used.
`ifndef RANDOMIZE_DELAY
  `define RANDOMIZE_DELAY 0.002
`endif // not def RANDOMIZE_DELAY

// Define INIT_RANDOM_PROLOG_ for use in our modules below.
`ifndef INIT_RANDOM_PROLOG_
  `ifdef RANDOMIZE
    `ifdef VERILATOR
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM
    `else  // VERILATOR
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM #`RANDOMIZE_DELAY begin end
    `endif // VERILATOR
  `else  // RANDOMIZE
    `define INIT_RANDOM_PROLOG_
  `endif // RANDOMIZE
`endif // not def INIT_RANDOM_PROLOG_

// Include register initializers in init blocks unless synthesis is set
`ifndef SYNTHESIS
  `ifndef ENABLE_INITIAL_REG_
    `define ENABLE_INITIAL_REG_
  `endif // not def ENABLE_INITIAL_REG_
`endif // not def SYNTHESIS

// Include rmemory initializers in init blocks unless synthesis is set
`ifndef SYNTHESIS
  `ifndef ENABLE_INITIAL_MEM_
    `define ENABLE_INITIAL_MEM_
  `endif // not def ENABLE_INITIAL_MEM_
`endif // not def SYNTHESIS

module GCD(	// src/main/scala/gcd/GCD.scala:14:7
  input         clock,	// src/main/scala/gcd/GCD.scala:14:7
                reset,	// src/main/scala/gcd/GCD.scala:14:7
  input  [15:0] io_value1,	// src/main/scala/gcd/GCD.scala:15:14
                io_value2,	// src/main/scala/gcd/GCD.scala:15:14
  input         io_loadingValues,	// src/main/scala/gcd/GCD.scala:15:14
  output [15:0] io_outputGCD,	// src/main/scala/gcd/GCD.scala:15:14
  output        io_outputValid	// src/main/scala/gcd/GCD.scala:15:14
);

  reg [15:0] x;	// src/main/scala/gcd/GCD.scala:23:15
  reg [15:0] y;	// src/main/scala/gcd/GCD.scala:24:15
  always @(posedge clock) begin	// src/main/scala/gcd/GCD.scala:14:7
    if (io_loadingValues) begin	// src/main/scala/gcd/GCD.scala:15:14
      x <= io_value1;	// src/main/scala/gcd/GCD.scala:23:15
      y <= io_value2;	// src/main/scala/gcd/GCD.scala:24:15
    end
    else if (x > y)	// src/main/scala/gcd/GCD.scala:23:15, :24:15, :26:10
      x <= x - y;	// src/main/scala/gcd/GCD.scala:23:15, :24:15, :26:24
    else	// src/main/scala/gcd/GCD.scala:26:10
      y <= y - x;	// src/main/scala/gcd/GCD.scala:23:15, :24:15, :27:25
  end // always @(posedge)
  `ifdef ENABLE_INITIAL_REG_	// src/main/scala/gcd/GCD.scala:14:7
    `ifdef FIRRTL_BEFORE_INITIAL	// src/main/scala/gcd/GCD.scala:14:7
      `FIRRTL_BEFORE_INITIAL	// src/main/scala/gcd/GCD.scala:14:7
    `endif // FIRRTL_BEFORE_INITIAL
    initial begin	// src/main/scala/gcd/GCD.scala:14:7
      automatic logic [31:0] _RANDOM[0:0];	// src/main/scala/gcd/GCD.scala:14:7
      `ifdef INIT_RANDOM_PROLOG_	// src/main/scala/gcd/GCD.scala:14:7
        `INIT_RANDOM_PROLOG_	// src/main/scala/gcd/GCD.scala:14:7
      `endif // INIT_RANDOM_PROLOG_
      `ifdef RANDOMIZE_REG_INIT	// src/main/scala/gcd/GCD.scala:14:7
        _RANDOM[/*Zero width*/ 1'b0] = `RANDOM;	// src/main/scala/gcd/GCD.scala:14:7
        x = _RANDOM[/*Zero width*/ 1'b0][15:0];	// src/main/scala/gcd/GCD.scala:14:7, :23:15
        y = _RANDOM[/*Zero width*/ 1'b0][31:16];	// src/main/scala/gcd/GCD.scala:14:7, :23:15, :24:15
      `endif // RANDOMIZE_REG_INIT
    end // initial
    `ifdef FIRRTL_AFTER_INITIAL	// src/main/scala/gcd/GCD.scala:14:7
      `FIRRTL_AFTER_INITIAL	// src/main/scala/gcd/GCD.scala:14:7
    `endif // FIRRTL_AFTER_INITIAL
  `endif // ENABLE_INITIAL_REG_
  assign io_outputGCD = x;	// src/main/scala/gcd/GCD.scala:14:7, :23:15
  assign io_outputValid = y == 16'h0;	// src/main/scala/gcd/GCD.scala:14:7, :24:15, :35:23
endmodule

