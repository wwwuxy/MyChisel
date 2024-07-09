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
