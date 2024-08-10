module ebreak(
    input logic is_ebreak
);
    import "DPI-C" function void npc_trap(input bit flag);
    
    always @(*) begin
        npc_trap(is_ebreak);
    end

endmodule
