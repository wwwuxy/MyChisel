module Memory(
    input clk,
    input [31:0] pc,
    input [31:0] alu_out,
    input [31:0] data,
    input wr_en,    
    input rd_en,    
    output [31:0] inst,
    output reg [31:0] dm_out
);
    wire [31:0] len;
    assign len = 32'd4;

    import "DPI-C" function int mem_read(input int pc, int len);

    import "DPI-C" function void mem_write(input int addr, int len, input int data);

    assign inst = mem_read(pc, len);

    always @(*) begin
        
        if (rd_en) begin
            dm_out = mem_read(alu_out, len);
        end
        else begin
            dm_out = alu_out;
        end
      
        if (wr_en) begin
            mem_write(alu_out, len, data);
        end
    end


endmodule
