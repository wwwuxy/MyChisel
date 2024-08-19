module npc_pc(
    input [31:0] pc
);
    import "DPI-C" function void npc_dpic_pc(input int now_pc);
    
    always @(*) begin
        npc_dpic_pc(pc);
    end

endmodule