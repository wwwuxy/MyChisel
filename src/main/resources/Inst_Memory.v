module Inst_Memory(
    input clk,
    input [31:0] pc,
    output [31:0] inst
);

    import "DPI-C" function int mem_read(input int pc, int len);

    assign inst = mem_read(pc, 4);
    
endmodule
