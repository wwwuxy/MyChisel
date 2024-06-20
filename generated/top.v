`ifndef RANDOMIZE
  `ifdef RANDOMIZE_MEM_INIT
    `define RANDOMIZE
  `endif 
`endif 
`ifndef RANDOMIZE
  `ifdef RANDOMIZE_REG_INIT
    `define RANDOMIZE
  `endif 
`endif 

`ifndef RANDOM
  `define RANDOM $random
`endif 

`ifndef INIT_RANDOM
  `define INIT_RANDOM
`endif 

`ifndef RANDOMIZE_DELAY
  `define RANDOMIZE_DELAY 0.002
`endif 

`ifndef INIT_RANDOM_PROLOG_
  `ifdef RANDOMIZE
    `ifdef VERILATOR
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM
    `else  
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM #`RANDOMIZE_DELAY begin end
    `endif 
  `else  
    `define INIT_RANDOM_PROLOG_
  `endif 
`endif 

`ifndef SYNTHESIS
  `ifndef ENABLE_INITIAL_REG_
    `define ENABLE_INITIAL_REG_
  `endif 
`endif 

`ifndef SYNTHESIS
  `ifndef ENABLE_INITIAL_MEM_
    `define ENABLE_INITIAL_MEM_
  `endif 
`endif 

module pc(	
  input         clock,	
                reset,	
  output [31:0] io_next_pc	
);

  reg [31:0] pc;	
  always @(posedge clock) begin	
    if (reset)	
      pc <= 32'h80000000;	
    else	
      pc <= pc + 32'h4;	
  end 
  `ifdef ENABLE_INITIAL_REG_	
    `ifdef FIRRTL_BEFORE_INITIAL	
      `FIRRTL_BEFORE_INITIAL	
    `endif 
    initial begin	
      automatic logic [31:0] _RANDOM[0:0];	
      `ifdef INIT_RANDOM_PROLOG_	
        `INIT_RANDOM_PROLOG_	
      `endif 
      `ifdef RANDOMIZE_REG_INIT	
        _RANDOM[/*Zero width*/ 1'b0] = `RANDOM;	
        pc = _RANDOM[/*Zero width*/ 1'b0];	
      `endif 
    end 
    `ifdef FIRRTL_AFTER_INITIAL	
      `FIRRTL_AFTER_INITIAL	
    `endif 
  `endif 
  assign io_next_pc = pc;	
endmodule

module alu(	
  input  [31:0] io_op1,	
                io_op2,	
  input  [11:0] io_alu_sel,	
  output [31:0] io_rsl	
);

  wire [62:0] umovl = {31'h0, io_op1} << io_op2[4:0];	
  wire [31:0] _smovr_T_2 = io_op1 >> io_op2;	
  assign io_rsl =
    (io_alu_sel[0] ? io_op1 + io_op2 : 32'h0) | (io_alu_sel[1] ? io_op1 - io_op2 : 32'h0)
    | (io_alu_sel[2] ? umovl[31:0] : 32'h0)
    | (io_alu_sel[3] ? io_op1 >> io_op2[4:0] : 32'h0)
    | (io_alu_sel[4] ? {1'h0, {4{io_op1[31]}}, _smovr_T_2[26:0]} : 32'h0)
    | (io_alu_sel[5] ? {31'h0, io_op1 == 32'h0} : 32'h0)
    | (io_alu_sel[6] ? io_op2 : 32'h0) | (io_alu_sel[7] ? io_op1 & io_op2 : 32'h0)
    | (io_alu_sel[8] ? io_op1 | io_op2 : 32'h0)
    | (io_alu_sel[9] ? io_op1 ^ io_op2 : 32'h0)
    | (io_alu_sel[10] ? {31'h0, io_op1 < io_op2} : 32'h0)
    | (io_alu_sel[11] ? {31'h0, io_op1 == io_op2} : 32'h0);	
endmodule

module registerfile(	
  input         clock,	
                reset,	
  input  [31:0] io_inst,	
  output [31:0] io_rd1,	
                io_rd2	
);

  reg  [31:0]       FileReg_0;	
  reg  [31:0]       FileReg_1;	
  reg  [31:0]       FileReg_2;	
  reg  [31:0]       FileReg_3;	
  reg  [31:0]       FileReg_4;	
  reg  [31:0]       FileReg_5;	
  reg  [31:0]       FileReg_6;	
  reg  [31:0]       FileReg_7;	
  reg  [31:0]       FileReg_8;	
  reg  [31:0]       FileReg_9;	
  reg  [31:0]       FileReg_10;	
  reg  [31:0]       FileReg_11;	
  reg  [31:0]       FileReg_12;	
  reg  [31:0]       FileReg_13;	
  reg  [31:0]       FileReg_14;	
  reg  [31:0]       FileReg_15;	
  reg  [31:0]       FileReg_16;	
  reg  [31:0]       FileReg_17;	
  reg  [31:0]       FileReg_18;	
  reg  [31:0]       FileReg_19;	
  reg  [31:0]       FileReg_20;	
  reg  [31:0]       FileReg_21;	
  reg  [31:0]       FileReg_22;	
  reg  [31:0]       FileReg_23;	
  reg  [31:0]       FileReg_24;	
  reg  [31:0]       FileReg_25;	
  reg  [31:0]       FileReg_26;	
  reg  [31:0]       FileReg_27;	
  reg  [31:0]       FileReg_28;	
  reg  [31:0]       FileReg_29;	
  reg  [31:0]       FileReg_30;	
  reg  [31:0]       FileReg_31;	
  wire [31:0][31:0] _GEN =
    {{FileReg_31},
     {FileReg_30},
     {FileReg_29},
     {FileReg_28},
     {FileReg_27},
     {FileReg_26},
     {FileReg_25},
     {FileReg_24},
     {FileReg_23},
     {FileReg_22},
     {FileReg_21},
     {FileReg_20},
     {FileReg_19},
     {FileReg_18},
     {FileReg_17},
     {FileReg_16},
     {FileReg_15},
     {FileReg_14},
     {FileReg_13},
     {FileReg_12},
     {FileReg_11},
     {FileReg_10},
     {FileReg_9},
     {FileReg_8},
     {FileReg_7},
     {FileReg_6},
     {FileReg_5},
     {FileReg_4},
     {FileReg_3},
     {FileReg_2},
     {FileReg_1},
     {FileReg_0}};	
  always @(posedge clock) begin	
    if (reset) begin	
      FileReg_0 <= 32'h0;	
      FileReg_1 <= 32'h0;	
      FileReg_2 <= 32'h0;	
      FileReg_3 <= 32'h0;	
      FileReg_4 <= 32'h0;	
      FileReg_5 <= 32'h0;	
      FileReg_6 <= 32'h0;	
      FileReg_7 <= 32'h0;	
      FileReg_8 <= 32'h0;	
      FileReg_9 <= 32'h0;	
      FileReg_10 <= 32'h0;	
      FileReg_11 <= 32'h0;	
      FileReg_12 <= 32'h0;	
      FileReg_13 <= 32'h0;	
      FileReg_14 <= 32'h0;	
      FileReg_15 <= 32'h0;	
      FileReg_16 <= 32'h0;	
      FileReg_17 <= 32'h0;	
      FileReg_18 <= 32'h0;	
      FileReg_19 <= 32'h0;	
      FileReg_20 <= 32'h0;	
      FileReg_21 <= 32'h0;	
      FileReg_22 <= 32'h0;	
      FileReg_23 <= 32'h0;	
      FileReg_24 <= 32'h0;	
      FileReg_25 <= 32'h0;	
      FileReg_26 <= 32'h0;	
      FileReg_27 <= 32'h0;	
      FileReg_28 <= 32'h0;	
      FileReg_29 <= 32'h0;	
      FileReg_30 <= 32'h0;	
      FileReg_31 <= 32'h0;	
    end
    else begin	
      automatic logic [31:0] _GEN_0;	
      _GEN_0 = _GEN[io_inst[11:7]];	
      if (io_inst[11:7] == 5'h0)	
        FileReg_0 <= _GEN_0;	
      else	
        FileReg_0 <= 32'h0;	
      if (io_inst[11:7] == 5'h1)	
        FileReg_1 <= _GEN_0;	
      if (io_inst[11:7] == 5'h2)	
        FileReg_2 <= _GEN_0;	
      if (io_inst[11:7] == 5'h3)	
        FileReg_3 <= _GEN_0;	
      if (io_inst[11:7] == 5'h4)	
        FileReg_4 <= _GEN_0;	
      if (io_inst[11:7] == 5'h5)	
        FileReg_5 <= _GEN_0;	
      if (io_inst[11:7] == 5'h6)	
        FileReg_6 <= _GEN_0;	
      if (io_inst[11:7] == 5'h7)	
        FileReg_7 <= _GEN_0;	
      if (io_inst[11:7] == 5'h8)	
        FileReg_8 <= _GEN_0;	
      if (io_inst[11:7] == 5'h9)	
        FileReg_9 <= _GEN_0;	
      if (io_inst[11:7] == 5'hA)	
        FileReg_10 <= _GEN_0;	
      if (io_inst[11:7] == 5'hB)	
        FileReg_11 <= _GEN_0;	
      if (io_inst[11:7] == 5'hC)	
        FileReg_12 <= _GEN_0;	
      if (io_inst[11:7] == 5'hD)	
        FileReg_13 <= _GEN_0;	
      if (io_inst[11:7] == 5'hE)	
        FileReg_14 <= _GEN_0;	
      if (io_inst[11:7] == 5'hF)	
        FileReg_15 <= _GEN_0;	
      if (io_inst[11:7] == 5'h10)	
        FileReg_16 <= _GEN_0;	
      if (io_inst[11:7] == 5'h11)	
        FileReg_17 <= _GEN_0;	
      if (io_inst[11:7] == 5'h12)	
        FileReg_18 <= _GEN_0;	
      if (io_inst[11:7] == 5'h13)	
        FileReg_19 <= _GEN_0;	
      if (io_inst[11:7] == 5'h14)	
        FileReg_20 <= _GEN_0;	
      if (io_inst[11:7] == 5'h15)	
        FileReg_21 <= _GEN_0;	
      if (io_inst[11:7] == 5'h16)	
        FileReg_22 <= _GEN_0;	
      if (io_inst[11:7] == 5'h17)	
        FileReg_23 <= _GEN_0;	
      if (io_inst[11:7] == 5'h18)	
        FileReg_24 <= _GEN_0;	
      if (io_inst[11:7] == 5'h19)	
        FileReg_25 <= _GEN_0;	
      if (io_inst[11:7] == 5'h1A)	
        FileReg_26 <= _GEN_0;	
      if (io_inst[11:7] == 5'h1B)	
        FileReg_27 <= _GEN_0;	
      if (io_inst[11:7] == 5'h1C)	
        FileReg_28 <= _GEN_0;	
      if (io_inst[11:7] == 5'h1D)	
        FileReg_29 <= _GEN_0;	
      if (io_inst[11:7] == 5'h1E)	
        FileReg_30 <= _GEN_0;	
      if (&(io_inst[11:7]))	
        FileReg_31 <= _GEN_0;	
    end
  end 
  `ifdef ENABLE_INITIAL_REG_	
    `ifdef FIRRTL_BEFORE_INITIAL	
      `FIRRTL_BEFORE_INITIAL	
    `endif 
    initial begin	
      automatic logic [31:0] _RANDOM[0:31];	
      `ifdef INIT_RANDOM_PROLOG_	
        `INIT_RANDOM_PROLOG_	
      `endif 
      `ifdef RANDOMIZE_REG_INIT	
        for (logic [5:0] i = 6'h0; i < 6'h20; i += 6'h1) begin
          _RANDOM[i[4:0]] = `RANDOM;	
        end	
        FileReg_0 = _RANDOM[5'h0];	
        FileReg_1 = _RANDOM[5'h1];	
        FileReg_2 = _RANDOM[5'h2];	
        FileReg_3 = _RANDOM[5'h3];	
        FileReg_4 = _RANDOM[5'h4];	
        FileReg_5 = _RANDOM[5'h5];	
        FileReg_6 = _RANDOM[5'h6];	
        FileReg_7 = _RANDOM[5'h7];	
        FileReg_8 = _RANDOM[5'h8];	
        FileReg_9 = _RANDOM[5'h9];	
        FileReg_10 = _RANDOM[5'hA];	
        FileReg_11 = _RANDOM[5'hB];	
        FileReg_12 = _RANDOM[5'hC];	
        FileReg_13 = _RANDOM[5'hD];	
        FileReg_14 = _RANDOM[5'hE];	
        FileReg_15 = _RANDOM[5'hF];	
        FileReg_16 = _RANDOM[5'h10];	
        FileReg_17 = _RANDOM[5'h11];	
        FileReg_18 = _RANDOM[5'h12];	
        FileReg_19 = _RANDOM[5'h13];	
        FileReg_20 = _RANDOM[5'h14];	
        FileReg_21 = _RANDOM[5'h15];	
        FileReg_22 = _RANDOM[5'h16];	
        FileReg_23 = _RANDOM[5'h17];	
        FileReg_24 = _RANDOM[5'h18];	
        FileReg_25 = _RANDOM[5'h19];	
        FileReg_26 = _RANDOM[5'h1A];	
        FileReg_27 = _RANDOM[5'h1B];	
        FileReg_28 = _RANDOM[5'h1C];	
        FileReg_29 = _RANDOM[5'h1D];	
        FileReg_30 = _RANDOM[5'h1E];	
        FileReg_31 = _RANDOM[5'h1F];	
      `endif 
    end 
    `ifdef FIRRTL_AFTER_INITIAL	
      `FIRRTL_AFTER_INITIAL	
    `endif 
  `endif 
  assign io_rd1 = _GEN[io_inst[19:15]];	
  assign io_rd2 = _GEN[io_inst[24:20]];	
endmodule

module controller(	
  input  [31:0] io_inst,	
  output [11:0] io_alu_sel,	
  output        io_nemutrap	
);

  assign io_alu_sel = {11'h0, io_inst[6:0] == 7'h13 & io_inst[14:12] == 3'h0};	
  assign io_nemutrap = io_inst == 32'h100073;	
endmodule

module mem_1024x32(	
  input  [9:0]  R0_addr,
  input         R0_en,
                R0_clk,
  output [31:0] R0_data
);

  reg [31:0] Memory[0:1023];	
  reg        _R0_en_d0;	
  reg [9:0]  _R0_addr_d0;	
  always @(posedge R0_clk) begin	
    _R0_en_d0 <= R0_en;	
    _R0_addr_d0 <= R0_addr;	
  end 
  `ifdef ENABLE_INITIAL_MEM_	
    `ifdef RANDOMIZE_REG_INIT	
      reg [31:0] _RANDOM;	
    `endif 
    reg [31:0] _RANDOM_MEM;	
    initial begin	
      `INIT_RANDOM_PROLOG_	
      `ifdef RANDOMIZE_MEM_INIT	
        for (logic [10:0] i = 11'h0; i < 11'h400; i += 11'h1) begin
          _RANDOM_MEM = `RANDOM;	
          Memory[i[9:0]] = _RANDOM_MEM;	
        end	
      `endif 
      `ifdef RANDOMIZE_REG_INIT	
        _RANDOM = {`RANDOM};	
        _R0_en_d0 = _RANDOM[0];	
        _R0_addr_d0 = _RANDOM[10:1];	
      `endif 
    end 
  `endif 
  assign R0_data = _R0_en_d0 ? Memory[_R0_addr_d0] : 32'bx;	
endmodule

module mem(	
  input         clock,	
  input  [31:0] io_im_addr,	
                io_dm_addr,	
  output [31:0] io_im_out	
);

  mem_1024x32 mem_ext (	
    .R0_addr (io_im_addr[9:0]),	
    .R0_en   (1'h1),	
    .R0_clk  (clock),
    .R0_data (io_im_out)
  );
endmodule

module top(	
  input  clock,	
         reset,	
  output io_nemutrap	
);

  wire [31:0] _Mem_io_im_out;	
  wire [11:0] _Controller_io_alu_sel;	
  wire [31:0] _RegisterFile_io_rd1;	
  wire [31:0] _RegisterFile_io_rd2;	
  wire [31:0] _Alu_io_rsl;	
  wire [31:0] _Pc_io_next_pc;	
  pc Pc (	
    .clock      (clock),
    .reset      (reset),
    .io_next_pc (_Pc_io_next_pc)
  );
  alu Alu (	
    .io_op1     (_RegisterFile_io_rd1),	
    .io_op2     (_RegisterFile_io_rd2),	
    .io_alu_sel (_Controller_io_alu_sel),	
    .io_rsl     (_Alu_io_rsl)
  );
  registerfile RegisterFile (	
    .clock   (clock),
    .reset   (reset),
    .io_inst (_Mem_io_im_out),	
    .io_rd1  (_RegisterFile_io_rd1),
    .io_rd2  (_RegisterFile_io_rd2)
  );
  controller Controller (	
    .io_inst     (_Mem_io_im_out),	
    .io_alu_sel  (_Controller_io_alu_sel),
    .io_nemutrap (io_nemutrap)
  );
  mem Mem (	
    .clock      (clock),
    .io_im_addr (_Pc_io_next_pc),	
    .io_dm_addr (_Alu_io_rsl),	
    .io_im_out  (_Mem_io_im_out)
  );
endmodule



module mem_1024x32_init();	
  initial	
    $readmemh("mem.hex", mem_1024x32.Memory);	
endmodule

bind mem_1024x32 mem_1024x32_init mem_1024x32_init ();
