module npc_inst(
    input [31:0] inst
);
    import "DPI-C" function void npc_inst(input int now_inst);
    
    always @(*) begin
        npc_inst(inst);
    end

endmodule