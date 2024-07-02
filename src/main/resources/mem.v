module mem{
    input clk,
    input [31:0] pc,
    input [31:0] addr,
    input [31:0] data,
    input wren,
    output [31:0] inst
    output [31:0] dmout
};
    reg [31:0] inst;

//read mem
    import "DPI-C" function int read_mem(input int pc);
    initial begin
        inst = read_mem(pc);
    end

//debug
    always @(pc) begin
        $display("pc = %d", pc);
        $display("inst = %d", inst);
    end

//output inst
    assign data = inst;


//write mem
    if(wren) begin
        import "DPI-C" function void write_mem(input int addr, input int data);
        initial begin
            write_mem(addr, data);
        end
    end

endmodule