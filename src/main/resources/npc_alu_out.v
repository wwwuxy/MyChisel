module npc_alu_out(
    input [31:0] alu_out
);
    import "DPI-C" function void npc_alu_out(input int now_alu_out);
    
    always @(*) begin
        npc_alu_out(alu_out);
    end

endmodule