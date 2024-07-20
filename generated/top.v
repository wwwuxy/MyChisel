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
                io_jump_jalr,	
  input  [31:0] io_jump_pc,	
  input         io_jump_en,	
                io_is_ecall,	
  input  [31:0] io_mtvec,	
  output [31:0] io_dnpc,	
                io_next_pc	
);

  reg  [31:0] pc;	
  wire        _GEN = io_jump_jalr & ~io_jump_en;	
  wire        _GEN_0 = io_jump_en & ~io_jump_jalr;	
  always @(posedge clock) begin	
    if (reset)	
      pc <= 32'h80000000;	
    else if (_GEN)	
      pc <= {io_jump_pc[31:1], 1'h0};	
    else if (_GEN_0)	
      pc <= io_jump_pc;	
    else if (io_is_ecall)	
      pc <= io_mtvec;	
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
  assign io_dnpc = _GEN ? pc + 32'h4 : _GEN_0 ? pc + 32'h4 : pc;	
  assign io_next_pc = pc;	
endmodule

module alu(	
  input  [31:0] io_op1,	
                io_op2,	
  input  [12:0] io_alu_sel,	
  output [31:0] io_rsl	
);

  wire [62:0] umovl = {31'h0, io_op1} << io_op2[4:0];	
  assign io_rsl =
    (io_alu_sel[0] ? io_op1 + io_op2 : 32'h0) | (io_alu_sel[1] ? io_op1 - io_op2 : 32'h0)
    | (io_alu_sel[2] ? umovl[31:0] : 32'h0)
    | (io_alu_sel[3] ? io_op1 >> io_op2[4:0] : 32'h0)
    | (io_alu_sel[4] ? $signed($signed(io_op1) >>> io_op2[4:0]) : 32'h0)
    | (io_alu_sel[5] ? ~io_op1 : 32'h0) | (io_alu_sel[6] ? io_op2 : 32'h0)
    | (io_alu_sel[7] ? io_op1 & io_op2 : 32'h0)
    | (io_alu_sel[8] ? io_op1 | io_op2 : 32'h0)
    | (io_alu_sel[9] ? io_op1 ^ io_op2 : 32'h0)
    | (io_alu_sel[10] ? {31'h0, io_op1 < io_op2} : 32'h0)
    | (io_alu_sel[11] ? {31'h0, $signed(io_op1) < $signed(io_op2)} : 32'h0)
    | (io_alu_sel[12] ? {31'h0, io_op1 == io_op2} : 32'h0);	
endmodule

module registerfile(	
  input         clock,	
                reset,	
  input  [31:0] io_inst,	
  input         io_wr_en,	
  input  [31:0] io_wd,	
  input         io_is_csr,	
                io_is_ecall,	
  input  [31:0] io_pc,	
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
  reg  [31:0]       CsrReg_0;	
  reg  [31:0]       CsrReg_1;	
  reg  [31:0]       CsrReg_2;	
  reg  [31:0]       CsrReg_3;	
  reg  [31:0]       t;	
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
  wire [31:0]       _GEN_0 = _GEN[io_inst[19:15]];	
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
      CsrReg_0 <= 32'h0;	
      CsrReg_1 <= 32'h0;	
      CsrReg_2 <= 32'h0;	
      CsrReg_3 <= 32'h0;	
      t <= 32'h0;	
    end
    else begin	
      automatic logic [1:0]  csr_sel;	
      automatic logic        _GEN_1;	
      automatic logic        _GEN_2;	
      automatic logic        _GEN_3;	
      automatic logic        _GEN_4;	
      automatic logic        _GEN_5;	
      automatic logic        _GEN_6;	
      automatic logic        _GEN_7;	
      automatic logic        _GEN_8;	
      automatic logic        _GEN_9;	
      automatic logic        _GEN_10;	
      automatic logic        _GEN_11;	
      automatic logic        _GEN_12;	
      automatic logic        _GEN_13;	
      automatic logic        _GEN_14;	
      automatic logic        _GEN_15;	
      automatic logic        _GEN_16;	
      automatic logic        _GEN_17;	
      automatic logic        _GEN_18;	
      automatic logic        _GEN_19;	
      automatic logic        _GEN_20;	
      automatic logic        _GEN_21;	
      automatic logic        _GEN_22;	
      automatic logic        _GEN_23;	
      automatic logic        _GEN_24;	
      automatic logic        _GEN_25;	
      automatic logic        _GEN_26;	
      automatic logic        _GEN_27;	
      automatic logic        _GEN_28;	
      automatic logic        _GEN_29;	
      automatic logic        _GEN_30;	
      automatic logic        _GEN_31;	
      automatic logic        _GEN_32;	
      automatic logic        _GEN_33;	
      automatic logic [31:0] _CsrReg_T;	
      automatic logic        _GEN_34;	
      automatic logic        _GEN_35;	
      automatic logic [31:0] _CsrReg_T_2;	
      csr_sel =
        io_inst[31:20] == 12'h300
          ? 2'h0
          : io_inst[31:20] == 12'h305
              ? 2'h1
              : io_inst[31:20] == 12'h341 ? 2'h2 : {2{io_inst[31:20] == 12'h342}};	
      _GEN_1 = io_inst[14:12] == 3'h1;	
      _GEN_2 = csr_sel == 2'h0;	
      _GEN_3 = io_inst[11:7] == 5'h1;	
      _GEN_4 = io_inst[11:7] == 5'h2;	
      _GEN_5 = io_inst[11:7] == 5'h3;	
      _GEN_6 = io_inst[11:7] == 5'h4;	
      _GEN_7 = io_inst[11:7] == 5'h5;	
      _GEN_8 = io_inst[11:7] == 5'h6;	
      _GEN_9 = io_inst[11:7] == 5'h7;	
      _GEN_10 = io_inst[11:7] == 5'h8;	
      _GEN_11 = io_inst[11:7] == 5'h9;	
      _GEN_12 = io_inst[11:7] == 5'hA;	
      _GEN_13 = io_inst[11:7] == 5'hB;	
      _GEN_14 = io_inst[11:7] == 5'hC;	
      _GEN_15 = io_inst[11:7] == 5'hD;	
      _GEN_16 = io_inst[11:7] == 5'hE;	
      _GEN_17 = io_inst[11:7] == 5'hF;	
      _GEN_18 = io_inst[11:7] == 5'h10;	
      _GEN_19 = io_inst[11:7] == 5'h11;	
      _GEN_20 = io_inst[11:7] == 5'h12;	
      _GEN_21 = io_inst[11:7] == 5'h13;	
      _GEN_22 = io_inst[11:7] == 5'h14;	
      _GEN_23 = io_inst[11:7] == 5'h15;	
      _GEN_24 = io_inst[11:7] == 5'h16;	
      _GEN_25 = io_inst[11:7] == 5'h17;	
      _GEN_26 = io_inst[11:7] == 5'h18;	
      _GEN_27 = io_inst[11:7] == 5'h19;	
      _GEN_28 = io_inst[11:7] == 5'h1A;	
      _GEN_29 = io_inst[11:7] == 5'h1B;	
      _GEN_30 = io_inst[11:7] == 5'h1C;	
      _GEN_31 = io_inst[11:7] == 5'h1D;	
      _GEN_32 = io_inst[11:7] == 5'h1E;	
      _GEN_33 = io_inst[14:12] == 3'h2;	
      _CsrReg_T = t | _GEN_0;	
      _GEN_34 = io_inst[14:12] == 3'h3;	
      _GEN_35 = _GEN_1 | _GEN_33 | _GEN_34;	
      _CsrReg_T_2 = t & ~_GEN_0;	
      if (io_is_csr) begin	
        automatic logic _GEN_36;	
        automatic logic _GEN_37;	
        _GEN_36 = csr_sel == 2'h1;	
        _GEN_37 = csr_sel == 2'h2;	
        if (_GEN_35 & io_inst[11:7] == 5'h0)	
          FileReg_0 <= t;	
        if (_GEN_35 & _GEN_3)	
          FileReg_1 <= t;	
        if (_GEN_35 & _GEN_4)	
          FileReg_2 <= t;	
        if (_GEN_35 & _GEN_5)	
          FileReg_3 <= t;	
        if (_GEN_35 & _GEN_6)	
          FileReg_4 <= t;	
        if (_GEN_35 & _GEN_7)	
          FileReg_5 <= t;	
        if (_GEN_35 & _GEN_8)	
          FileReg_6 <= t;	
        if (_GEN_35 & _GEN_9)	
          FileReg_7 <= t;	
        if (_GEN_35 & _GEN_10)	
          FileReg_8 <= t;	
        if (_GEN_35 & _GEN_11)	
          FileReg_9 <= t;	
        if (_GEN_35 & _GEN_12)	
          FileReg_10 <= t;	
        if (_GEN_35 & _GEN_13)	
          FileReg_11 <= t;	
        if (_GEN_35 & _GEN_14)	
          FileReg_12 <= t;	
        if (_GEN_35 & _GEN_15)	
          FileReg_13 <= t;	
        if (_GEN_35 & _GEN_16)	
          FileReg_14 <= t;	
        if (_GEN_35 & _GEN_17)	
          FileReg_15 <= t;	
        if (_GEN_35 & _GEN_18)	
          FileReg_16 <= t;	
        if (_GEN_35 & _GEN_19)	
          FileReg_17 <= t;	
        if (_GEN_35 & _GEN_20)	
          FileReg_18 <= t;	
        if (_GEN_35 & _GEN_21)	
          FileReg_19 <= t;	
        if (_GEN_35 & _GEN_22)	
          FileReg_20 <= t;	
        if (_GEN_35 & _GEN_23)	
          FileReg_21 <= t;	
        if (_GEN_35 & _GEN_24)	
          FileReg_22 <= t;	
        if (_GEN_35 & _GEN_25)	
          FileReg_23 <= t;	
        if (_GEN_35 & _GEN_26)	
          FileReg_24 <= t;	
        if (_GEN_35 & _GEN_27)	
          FileReg_25 <= t;	
        if (_GEN_35 & _GEN_28)	
          FileReg_26 <= t;	
        if (_GEN_35 & _GEN_29)	
          FileReg_27 <= t;	
        if (_GEN_35 & _GEN_30)	
          FileReg_28 <= t;	
        if (_GEN_35 & _GEN_31)	
          FileReg_29 <= t;	
        if (_GEN_35 & _GEN_32)	
          FileReg_30 <= t;	
        if (_GEN_35 & (&(io_inst[11:7])))	
          FileReg_31 <= t;	
        if (_GEN_1) begin	
          if (_GEN_36)	
            CsrReg_1 <= _GEN_0;	
          if (_GEN_37)	
            CsrReg_2 <= _GEN_0;	
          else if (io_is_ecall)	
            CsrReg_2 <= io_pc;	
          if (&csr_sel)	
            CsrReg_3 <= _GEN_0;	
          else if (io_is_ecall)	
            CsrReg_3 <= 32'hB;	
        end
        else if (_GEN_33) begin	
          if (_GEN_36)	
            CsrReg_1 <= _CsrReg_T;	
          if (_GEN_37)	
            CsrReg_2 <= _CsrReg_T;	
          else if (io_is_ecall)	
            CsrReg_2 <= io_pc;	
          if (&csr_sel)	
            CsrReg_3 <= _CsrReg_T;	
          else if (io_is_ecall)	
            CsrReg_3 <= 32'hB;	
        end
        else begin	
          if (_GEN_34 & _GEN_36)	
            CsrReg_1 <= _CsrReg_T_2;	
          if (_GEN_34 & _GEN_37)	
            CsrReg_2 <= _CsrReg_T_2;	
          else if (io_is_ecall)	
            CsrReg_2 <= io_pc;	
          if (_GEN_34 & (&csr_sel))	
            CsrReg_3 <= _CsrReg_T_2;	
          else if (io_is_ecall)	
            CsrReg_3 <= 32'hB;	
        end
      end
      else begin	
        automatic logic [31:0] _GEN_38;	
        _GEN_38 = _GEN[io_inst[11:7]];	
        FileReg_0 <= 32'h0;	
        if (_GEN_3) begin	
          if (io_wr_en)	
            FileReg_1 <= io_wd;	
          else	
            FileReg_1 <= _GEN_38;	
        end
        if (_GEN_4) begin	
          if (io_wr_en)	
            FileReg_2 <= io_wd;	
          else	
            FileReg_2 <= _GEN_38;	
        end
        if (_GEN_5) begin	
          if (io_wr_en)	
            FileReg_3 <= io_wd;	
          else	
            FileReg_3 <= _GEN_38;	
        end
        if (_GEN_6) begin	
          if (io_wr_en)	
            FileReg_4 <= io_wd;	
          else	
            FileReg_4 <= _GEN_38;	
        end
        if (_GEN_7) begin	
          if (io_wr_en)	
            FileReg_5 <= io_wd;	
          else	
            FileReg_5 <= _GEN_38;	
        end
        if (_GEN_8) begin	
          if (io_wr_en)	
            FileReg_6 <= io_wd;	
          else	
            FileReg_6 <= _GEN_38;	
        end
        if (_GEN_9) begin	
          if (io_wr_en)	
            FileReg_7 <= io_wd;	
          else	
            FileReg_7 <= _GEN_38;	
        end
        if (_GEN_10) begin	
          if (io_wr_en)	
            FileReg_8 <= io_wd;	
          else	
            FileReg_8 <= _GEN_38;	
        end
        if (_GEN_11) begin	
          if (io_wr_en)	
            FileReg_9 <= io_wd;	
          else	
            FileReg_9 <= _GEN_38;	
        end
        if (_GEN_12) begin	
          if (io_wr_en)	
            FileReg_10 <= io_wd;	
          else	
            FileReg_10 <= _GEN_38;	
        end
        if (_GEN_13) begin	
          if (io_wr_en)	
            FileReg_11 <= io_wd;	
          else	
            FileReg_11 <= _GEN_38;	
        end
        if (_GEN_14) begin	
          if (io_wr_en)	
            FileReg_12 <= io_wd;	
          else	
            FileReg_12 <= _GEN_38;	
        end
        if (_GEN_15) begin	
          if (io_wr_en)	
            FileReg_13 <= io_wd;	
          else	
            FileReg_13 <= _GEN_38;	
        end
        if (_GEN_16) begin	
          if (io_wr_en)	
            FileReg_14 <= io_wd;	
          else	
            FileReg_14 <= _GEN_38;	
        end
        if (_GEN_17) begin	
          if (io_wr_en)	
            FileReg_15 <= io_wd;	
          else	
            FileReg_15 <= _GEN_38;	
        end
        if (_GEN_18) begin	
          if (io_wr_en)	
            FileReg_16 <= io_wd;	
          else	
            FileReg_16 <= _GEN_38;	
        end
        if (_GEN_19) begin	
          if (io_wr_en)	
            FileReg_17 <= io_wd;	
          else	
            FileReg_17 <= _GEN_38;	
        end
        if (_GEN_20) begin	
          if (io_wr_en)	
            FileReg_18 <= io_wd;	
          else	
            FileReg_18 <= _GEN_38;	
        end
        if (_GEN_21) begin	
          if (io_wr_en)	
            FileReg_19 <= io_wd;	
          else	
            FileReg_19 <= _GEN_38;	
        end
        if (_GEN_22) begin	
          if (io_wr_en)	
            FileReg_20 <= io_wd;	
          else	
            FileReg_20 <= _GEN_38;	
        end
        if (_GEN_23) begin	
          if (io_wr_en)	
            FileReg_21 <= io_wd;	
          else	
            FileReg_21 <= _GEN_38;	
        end
        if (_GEN_24) begin	
          if (io_wr_en)	
            FileReg_22 <= io_wd;	
          else	
            FileReg_22 <= _GEN_38;	
        end
        if (_GEN_25) begin	
          if (io_wr_en)	
            FileReg_23 <= io_wd;	
          else	
            FileReg_23 <= _GEN_38;	
        end
        if (_GEN_26) begin	
          if (io_wr_en)	
            FileReg_24 <= io_wd;	
          else	
            FileReg_24 <= _GEN_38;	
        end
        if (_GEN_27) begin	
          if (io_wr_en)	
            FileReg_25 <= io_wd;	
          else	
            FileReg_25 <= _GEN_38;	
        end
        if (_GEN_28) begin	
          if (io_wr_en)	
            FileReg_26 <= io_wd;	
          else	
            FileReg_26 <= _GEN_38;	
        end
        if (_GEN_29) begin	
          if (io_wr_en)	
            FileReg_27 <= io_wd;	
          else	
            FileReg_27 <= _GEN_38;	
        end
        if (_GEN_30) begin	
          if (io_wr_en)	
            FileReg_28 <= io_wd;	
          else	
            FileReg_28 <= _GEN_38;	
        end
        if (_GEN_31) begin	
          if (io_wr_en)	
            FileReg_29 <= io_wd;	
          else	
            FileReg_29 <= _GEN_38;	
        end
        if (_GEN_32) begin	
          if (io_wr_en)	
            FileReg_30 <= io_wd;	
          else	
            FileReg_30 <= _GEN_38;	
        end
        if (&(io_inst[11:7])) begin	
          if (io_wr_en)	
            FileReg_31 <= io_wd;	
          else	
            FileReg_31 <= _GEN_38;	
        end
        if (io_is_ecall) begin	
          CsrReg_2 <= io_pc;	
          CsrReg_3 <= 32'hB;	
        end
      end
      CsrReg_0 <=
        io_is_csr
          ? (_GEN_1
               ? (_GEN_2 ? _GEN_0 : 32'h1800)
               : _GEN_33
                   ? (_GEN_2 ? _CsrReg_T : 32'h1800)
                   : _GEN_34 & _GEN_2 ? _CsrReg_T_2 : 32'h1800)
          : 32'h1800;	
      if (io_is_csr & _GEN_35) begin	
        automatic logic [3:0][31:0] _GEN_39;	
        _GEN_39 = {{CsrReg_3}, {CsrReg_2}, {CsrReg_1}, {CsrReg_0}};	
        t <= _GEN_39[csr_sel];	
      end
    end
  end 
  `ifdef ENABLE_INITIAL_REG_	
    `ifdef FIRRTL_BEFORE_INITIAL	
      `FIRRTL_BEFORE_INITIAL	
    `endif 
    initial begin	
      automatic logic [31:0] _RANDOM[0:36];	
      `ifdef INIT_RANDOM_PROLOG_	
        `INIT_RANDOM_PROLOG_	
      `endif 
      `ifdef RANDOMIZE_REG_INIT	
        for (logic [5:0] i = 6'h0; i < 6'h25; i += 6'h1) begin
          _RANDOM[i] = `RANDOM;	
        end	
        FileReg_0 = _RANDOM[6'h0];	
        FileReg_1 = _RANDOM[6'h1];	
        FileReg_2 = _RANDOM[6'h2];	
        FileReg_3 = _RANDOM[6'h3];	
        FileReg_4 = _RANDOM[6'h4];	
        FileReg_5 = _RANDOM[6'h5];	
        FileReg_6 = _RANDOM[6'h6];	
        FileReg_7 = _RANDOM[6'h7];	
        FileReg_8 = _RANDOM[6'h8];	
        FileReg_9 = _RANDOM[6'h9];	
        FileReg_10 = _RANDOM[6'hA];	
        FileReg_11 = _RANDOM[6'hB];	
        FileReg_12 = _RANDOM[6'hC];	
        FileReg_13 = _RANDOM[6'hD];	
        FileReg_14 = _RANDOM[6'hE];	
        FileReg_15 = _RANDOM[6'hF];	
        FileReg_16 = _RANDOM[6'h10];	
        FileReg_17 = _RANDOM[6'h11];	
        FileReg_18 = _RANDOM[6'h12];	
        FileReg_19 = _RANDOM[6'h13];	
        FileReg_20 = _RANDOM[6'h14];	
        FileReg_21 = _RANDOM[6'h15];	
        FileReg_22 = _RANDOM[6'h16];	
        FileReg_23 = _RANDOM[6'h17];	
        FileReg_24 = _RANDOM[6'h18];	
        FileReg_25 = _RANDOM[6'h19];	
        FileReg_26 = _RANDOM[6'h1A];	
        FileReg_27 = _RANDOM[6'h1B];	
        FileReg_28 = _RANDOM[6'h1C];	
        FileReg_29 = _RANDOM[6'h1D];	
        FileReg_30 = _RANDOM[6'h1E];	
        FileReg_31 = _RANDOM[6'h1F];	
        CsrReg_0 = _RANDOM[6'h20];	
        CsrReg_1 = _RANDOM[6'h21];	
        CsrReg_2 = _RANDOM[6'h22];	
        CsrReg_3 = _RANDOM[6'h23];	
        t = _RANDOM[6'h24];	
      `endif 
    end 
    `ifdef FIRRTL_AFTER_INITIAL	
      `FIRRTL_AFTER_INITIAL	
    `endif 
  `endif 
  assign io_rd1 = io_is_csr ? 32'h0 : _GEN_0;	
  assign io_rd2 = io_is_csr ? (io_is_ecall ? CsrReg_1 : 32'h0) : _GEN[io_inst[24:20]];	
endmodule

module controller(	
  input  [31:0] io_inst,	
                io_rs1,	
                io_rs2,	
  output        io_rf_wr_en,	
  output [2:0]  io_rf_wr_sel,	
  output        io_alu_a_sel,	
                io_alu_b_sel,	
                io_mem_wr_en,	
                io_mem_rd_en,	
  output [12:0] io_alu_sel,	
  output        io_jump_en,	
                io_jump_jalr,	
  output [31:0] io_len,	
                io_imm,	
  output        io_load_unsign,	
                io_is_ecall,	
                io_is_csr	
);

  wire isR_type = io_inst[6:0] == 7'h33;	
  wire isI_type = io_inst[6:0] == 7'h13;	
  wire isS_type = io_inst[6:0] == 7'h23;	
  wire isB_type = io_inst[6:0] == 7'h63;	
  wire is_load = io_inst[6:0] == 7'h3;	
  wire is_auipc = io_inst[6:0] == 7'h17;	
  wire is_lui = io_inst[6:0] == 7'h37;	
  wire is_jal = io_inst[6:0] == 7'h6F;	
  wire is_jalr = io_inst[6:0] == 7'h67;	
  wire _io_is_csr_T_6 = io_inst[14:12] == 3'h5;	
  wire _io_is_csr_T_1 = io_inst[14:12] == 3'h1;	
  wire _GEN = is_jalr | is_jal | is_auipc;	
  wire _GEN_0 = io_inst[14:12] == 3'h0;	
  wire _GEN_1 = isB_type & _GEN_0;	
  wire _GEN_2 = isB_type & _io_is_csr_T_1;	
  wire _GEN_3 = io_inst[14:12] == 3'h4;	
  wire _GEN_4 = isB_type & _GEN_3;	
  wire _GEN_5 = isB_type & _io_is_csr_T_6;	
  wire _io_is_csr_T_8 = io_inst[14:12] == 3'h6;	
  wire _GEN_6 = isB_type & _io_is_csr_T_8;	
  wire _GEN_7 = isB_type & (&(io_inst[14:12]));	
  wire _GEN_8 = is_load & _GEN_0;	
  wire _GEN_9 = is_load & _io_is_csr_T_1;	
  wire _io_is_csr_T_2 = io_inst[14:12] == 3'h2;	
  wire _GEN_10 = is_load & _io_is_csr_T_2;	
  wire _GEN_11 = is_load & _GEN_3;	
  wire _GEN_12 = is_load & _io_is_csr_T_6;	
  wire _GEN_13 = _GEN_12 | _GEN_11 | _GEN_10 | _GEN_9 | _GEN_8;	
  wire _GEN_14 = isS_type & _GEN_0;	
  wire _GEN_15 = isS_type & _io_is_csr_T_1;	
  wire _GEN_16 = isS_type & _io_is_csr_T_2;	
  wire _GEN_17 = isI_type & _GEN_0;	
  wire _io_is_csr_T_4 = io_inst[14:12] == 3'h3;	
  wire _GEN_18 = isI_type & _io_is_csr_T_4;	
  wire _GEN_19 = isI_type & _GEN_3;	
  wire _GEN_20 = isI_type & _io_is_csr_T_8;	
  wire _GEN_21 = isI_type & (&(io_inst[14:12]));	
  wire _GEN_22 = io_inst[31:25] == 7'h0;	
  wire _GEN_23 = isI_type & _io_is_csr_T_1 & _GEN_22;	
  wire _GEN_24 = isI_type & _io_is_csr_T_6;	
  wire _GEN_25 = _GEN_24 & _GEN_22;	
  wire _GEN_26 = io_inst[31:25] == 7'h20;	
  wire _GEN_27 = _GEN_24 & _GEN_26;	
  wire _GEN_28 = isR_type & _GEN_0;	
  wire _GEN_29 = _GEN_28 & _GEN_22;	
  wire _GEN_30 = _GEN_28 & _GEN_26;	
  wire _GEN_31 = isR_type & _io_is_csr_T_1 & _GEN_22;	
  wire _GEN_32 = isR_type & _io_is_csr_T_2 & _GEN_22;	
  wire _GEN_33 = isR_type & _io_is_csr_T_4 & _GEN_22;	
  wire _GEN_34 = isR_type & _GEN_3 & _GEN_22;	
  wire _GEN_35 = isR_type & _io_is_csr_T_6;	
  wire _GEN_36 = _GEN_35 & _GEN_22;	
  wire _GEN_37 = _GEN_35 & _GEN_26;	
  wire _GEN_38 = isR_type & _io_is_csr_T_8 & _GEN_22;	
  wire _GEN_39 = isR_type & (&(io_inst[14:12])) & _GEN_22;	
  assign io_rf_wr_en =
    _GEN_39 | _GEN_38 | _GEN_37 | _GEN_36 | _GEN_34 | _GEN_33 | _GEN_32 | _GEN_31
    | _GEN_30 | _GEN_29 | _GEN_27 | _GEN_25 | _GEN_23 | _GEN_21 | _GEN_20 | _GEN_19
    | _GEN_18 | _GEN_17 | _GEN_12 | _GEN_11 | _GEN_10 | _GEN_9 | _GEN_8 | _GEN | is_lui;	
  assign io_rf_wr_sel =
    _GEN_39 | _GEN_38 | _GEN_37 | _GEN_36 | _GEN_34 | _GEN_33 | _GEN_32 | _GEN_31
    | _GEN_30 | _GEN_29
      ? 3'h2
      : _GEN_27 | _GEN_25 | _GEN_23 | _GEN_21 | _GEN_20 | _GEN_19 | _GEN_18 | _GEN_17
          ? 3'h1
          : _GEN_13 ? 3'h2 : is_jalr | is_jal ? 3'h4 : {2'h0, is_auipc | is_lui};	
  assign io_alu_a_sel =
    _GEN_39 | _GEN_38 | _GEN_37 | _GEN_36 | _GEN_34 | _GEN_33 | _GEN_32 | _GEN_31
    | _GEN_30 | _GEN_29 | _GEN_27 | _GEN_25 | _GEN_23 | _GEN_21 | _GEN_20 | _GEN_19
    | _GEN_18 | _GEN_17 | _GEN_16 | _GEN_15 | _GEN_14 | _GEN_13
    | ~(_GEN_7 | _GEN_6 | _GEN_5 | _GEN_4 | _GEN_2 | _GEN_1) & is_jalr;	
  assign io_alu_b_sel =
    _GEN_39 | _GEN_38 | _GEN_37 | _GEN_36 | _GEN_34 | _GEN_33 | _GEN_32 | _GEN_31
    | _GEN_30 | _GEN_29;	
  assign io_mem_wr_en = _GEN_16 | _GEN_15 | _GEN_14;	
  assign io_mem_rd_en = _GEN_12 | _GEN_11 | _GEN_10 | _GEN_9 | _GEN_8;	
  assign io_alu_sel =
    _GEN_39
      ? 13'h80
      : _GEN_38
          ? 13'h100
          : _GEN_37
              ? 13'h10
              : _GEN_36
                  ? 13'h8
                  : _GEN_34
                      ? 13'h200
                      : _GEN_33
                          ? 13'h400
                          : _GEN_32
                              ? 13'h800
                              : _GEN_31
                                  ? 13'h4
                                  : _GEN_30
                                      ? 13'h2
                                      : _GEN_29
                                          ? 13'h1
                                          : _GEN_27
                                              ? 13'h10
                                              : _GEN_25
                                                  ? 13'h8
                                                  : _GEN_23
                                                      ? 13'h4
                                                      : _GEN_21
                                                          ? 13'h80
                                                          : _GEN_20
                                                              ? 13'h100
                                                              : _GEN_19
                                                                  ? 13'h200
                                                                  : _GEN_18
                                                                      ? 13'h400
                                                                      : _GEN_17 | _GEN_16
                                                                        | _GEN_15
                                                                        | _GEN_14
                                                                        | _GEN_12
                                                                        | _GEN_11
                                                                        | _GEN_10 | _GEN_9
                                                                        | _GEN_8 | _GEN_7
                                                                        | _GEN_6 | _GEN_5
                                                                        | _GEN_4 | _GEN_2
                                                                        | _GEN_1 | _GEN
                                                                          ? 13'h1
                                                                          : {6'h0,
                                                                             is_lui,
                                                                             6'h0};	
  assign io_jump_en =
    _GEN_7
      ? io_rs1 >= io_rs2
      : _GEN_6
          ? io_rs1 < io_rs2
          : _GEN_5
              ? $signed(io_rs1) >= $signed(io_rs2)
              : _GEN_4
                  ? $signed(io_rs1) < $signed(io_rs2)
                  : _GEN_2 ? io_rs1 != io_rs2 : _GEN_1 ? io_rs1 == io_rs2 : is_jal;	
  assign io_jump_jalr = is_jalr;	
  assign io_len =
    _GEN_16
      ? 32'h4
      : _GEN_15
          ? 32'h2
          : _GEN_14
              ? 32'h1
              : _GEN_12
                  ? 32'h2
                  : _GEN_11
                      ? 32'h1
                      : _GEN_10 ? 32'h4 : _GEN_9 ? 32'h2 : _GEN_8 ? 32'h1 : 32'h4;	
  assign io_imm =
    isB_type
      ? {{20{io_inst[31]}}, io_inst[7], io_inst[30:25], io_inst[11:8], 1'h0}
      : isS_type
          ? {{20{io_inst[31]}}, io_inst[31:25], io_inst[11:7]}
          : is_load
              ? {{20{io_inst[31]}}, io_inst[31:20]}
              : is_jalr
                  ? {{20{io_inst[31]}}, io_inst[31:20]}
                  : is_jal
                      ? {{12{io_inst[31]}},
                         io_inst[19:12],
                         io_inst[20],
                         io_inst[30:21],
                         1'h0}
                      : is_lui
                          ? {io_inst[31:12], 12'h0}
                          : is_auipc
                              ? {io_inst[31:12], 12'h0}
                              : isI_type & (_io_is_csr_T_6 | _io_is_csr_T_1)
                                  ? {27'h0, io_inst[24:20]}
                                  : isI_type & io_inst[14:12] != 3'h5
                                    & io_inst[14:12] != 3'h1
                                      ? {{20{io_inst[31]}}, io_inst[31:20]}
                                      : 32'h0;	
  assign io_load_unsign = _GEN_12 | _GEN_11;	
  assign io_is_ecall = io_inst == 32'h73;	
  assign io_is_csr =
    io_inst[6:0] == 7'h73
    & (_io_is_csr_T_1 | _io_is_csr_T_2 | _io_is_csr_T_4 | _io_is_csr_T_6 | _io_is_csr_T_8
       | (&(io_inst[14:12])));	
endmodule

module inputalu(	
  input  [31:0] io_rs1,	
                io_rs2,	
                io_imm,	
                io_pc,	
  input         io_alu_a_sel,	
                io_alu_b_sel,	
  output [31:0] io_op1,	
                io_op2	
);

  assign io_op1 = io_alu_a_sel ? io_rs1 : io_pc;	
  assign io_op2 = io_alu_b_sel ? io_rs2 : io_imm;	
endmodule


module inputreg(	
  input  [31:0] io_dm_out,	
  input  [2:0]  io_rf_wr_sel,	
  input  [31:0] io_storepc,	
  output [31:0] io_wd	
);

  assign io_wd =
    (io_rf_wr_sel[0] | io_rf_wr_sel[1] ? io_dm_out : 32'h0)
    | (io_rf_wr_sel[2] ? io_storepc : 32'h0);	
endmodule

module top(	
  input         clock,	
                reset,	
  output [31:0] io_inst,	
                io_pc,	
                io_addr,	
                io_data,	
                io_nextpc,	
                io_alu_out,	
                io_alu_op1,	
                io_alu_op2,	
                io_imm	
);

  wire [31:0] _InputReg_io_wd;	
  wire [31:0] _Mem_inst;	
  wire [31:0] _Mem_dm_out;	
  wire [31:0] _InputAlu_io_op1;	
  wire [31:0] _InputAlu_io_op2;	
  wire        _Controller_io_rf_wr_en;	
  wire [2:0]  _Controller_io_rf_wr_sel;	
  wire        _Controller_io_alu_a_sel;	
  wire        _Controller_io_alu_b_sel;	
  wire        _Controller_io_mem_wr_en;	
  wire        _Controller_io_mem_rd_en;	
  wire [12:0] _Controller_io_alu_sel;	
  wire        _Controller_io_jump_en;	
  wire        _Controller_io_jump_jalr;	
  wire [31:0] _Controller_io_len;	
  wire [31:0] _Controller_io_imm;	
  wire        _Controller_io_load_unsign;	
  wire        _Controller_io_is_ecall;	
  wire        _Controller_io_is_csr;	
  wire [31:0] _RegisterFile_io_rd1;	
  wire [31:0] _RegisterFile_io_rd2;	
  wire [31:0] _Alu_io_rsl;	
  wire [31:0] _Pc_io_dnpc;	
  wire [31:0] _Pc_io_next_pc;	
  pc Pc (	
    .clock        (clock),
    .reset        (reset),
    .io_jump_jalr (_Controller_io_jump_jalr),	
    .io_jump_pc   (_Alu_io_rsl),	
    .io_jump_en   (_Controller_io_jump_en),	
    .io_is_ecall  (_Controller_io_is_ecall),	
    .io_mtvec     (_RegisterFile_io_rd2),	
    .io_dnpc      (_Pc_io_dnpc),
    .io_next_pc   (_Pc_io_next_pc)
  );
  alu Alu (	
    .io_op1     (_InputAlu_io_op1),	
    .io_op2     (_InputAlu_io_op2),	
    .io_alu_sel (_Controller_io_alu_sel),	
    .io_rsl     (_Alu_io_rsl)
  );
  registerfile RegisterFile (	
    .clock       (clock),
    .reset       (reset),
    .io_inst     (_Mem_inst),	
    .io_wr_en    (_Controller_io_rf_wr_en),	
    .io_wd       (_InputReg_io_wd),	
    .io_is_csr   (_Controller_io_is_csr),	
    .io_is_ecall (_Controller_io_is_ecall),	
    .io_pc       (_Pc_io_next_pc),	
    .io_rd1      (_RegisterFile_io_rd1),
    .io_rd2      (_RegisterFile_io_rd2)
  );
  controller Controller (	
    .io_inst        (_Mem_inst),	
    .io_rs1         (_RegisterFile_io_rd1),	
    .io_rs2         (_RegisterFile_io_rd2),	
    .io_rf_wr_en    (_Controller_io_rf_wr_en),
    .io_rf_wr_sel   (_Controller_io_rf_wr_sel),
    .io_alu_a_sel   (_Controller_io_alu_a_sel),
    .io_alu_b_sel   (_Controller_io_alu_b_sel),
    .io_mem_wr_en   (_Controller_io_mem_wr_en),
    .io_mem_rd_en   (_Controller_io_mem_rd_en),
    .io_alu_sel     (_Controller_io_alu_sel),
    .io_jump_en     (_Controller_io_jump_en),
    .io_jump_jalr   (_Controller_io_jump_jalr),
    .io_len         (_Controller_io_len),
    .io_imm         (_Controller_io_imm),
    .io_load_unsign (_Controller_io_load_unsign),
    .io_is_ecall    (_Controller_io_is_ecall),
    .io_is_csr      (_Controller_io_is_csr)
  );
  inputalu InputAlu (	
    .io_rs1       (_RegisterFile_io_rd1),	
    .io_rs2       (_RegisterFile_io_rd2),	
    .io_imm       (_Controller_io_imm),	
    .io_pc        (_Pc_io_next_pc),	
    .io_alu_a_sel (_Controller_io_alu_a_sel),	
    .io_alu_b_sel (_Controller_io_alu_b_sel),	
    .io_op1       (_InputAlu_io_op1),
    .io_op2       (_InputAlu_io_op2)
  );
  Memory Mem (	
    .pc          (_Pc_io_next_pc),	
    .alu_out     (_Alu_io_rsl),	
    .data        (_RegisterFile_io_rd2),	
    .wr_en       (_Controller_io_mem_wr_en),	
    .rd_en       (_Controller_io_mem_rd_en),	
    .len         (_Controller_io_len),	
    .load_unsign (_Controller_io_load_unsign),	
    .inst        (_Mem_inst),
    .dm_out      (_Mem_dm_out)
  );
  inputreg InputReg (	
    .io_dm_out    (_Mem_dm_out),	
    .io_rf_wr_sel (_Controller_io_rf_wr_sel),	
    .io_storepc   (_Pc_io_dnpc),	
    .io_wd        (_InputReg_io_wd)
  );
  assign io_inst = _Mem_inst;	
  assign io_pc = _Pc_io_next_pc;	
  assign io_addr = _Alu_io_rsl;	
  assign io_data = _RegisterFile_io_rd2;	
  assign io_nextpc = _Alu_io_rsl;	
  assign io_alu_out = _Alu_io_rsl;	
  assign io_alu_op1 = _InputAlu_io_op1;	
  assign io_alu_op2 = _InputAlu_io_op2;	
  assign io_imm = _Controller_io_imm;	
endmodule



module Memory(
    input clk,
    input [31:0] pc,
    input [31:0] alu_out,
    input [31:0] data,
    input wr_en,    
    input rd_en,
    input [31:0] len,
    input load_unsign,    
    output [31:0] inst,
    output reg [31:0] dm_out
);

    import "DPI-C" function int mem_read(input int pc, int len);

    import "DPI-C" function void mem_write(input int addr, int len, input int data);

    assign inst = mem_read(pc, 4);

    always @(*) begin
        reg [31:0] temp_data;
        if (rd_en) begin
            if(load_unsign) begin
                if(len == 1) begin
                    dm_out = {24'b0, mem_read(alu_out, 1)};
                end
                else if(len == 2) begin
                    dm_out = {16'b0, mem_read(alu_out, 2)};
                end
                else if(len == 4) begin
                    dm_out = mem_read(alu_out, 4);
                end
            end
            else begin
                temp_data = mem_read(alu_out, len);
                if(len == 1) begin
                    dm_out = {{24{temp_data[7]}}, temp_data[7:0]};
                end
                else if(len == 2) begin
                    dm_out = {{16{temp_data[15]}}, temp_data[15:0]};
                end
                else if(len == 4) begin
                    dm_out = mem_read(alu_out, 4);
                end
            
            end
        end
        else begin
            dm_out = alu_out;
        end
      
        if (wr_en) begin
            if(len == 1) begin
                mem_write(alu_out, 1, data);
            end
            else if(len == 2) begin
                mem_write(alu_out, 2, data);
            end
            else if(len == 4) begin
                mem_write(alu_out, 4, data);
            end
        end
    end


endmodule

