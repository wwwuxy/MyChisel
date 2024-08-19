module difftest(
    input logic start_difftest
);
    import "DPI-C" function void npc_difftest(input bit flag);
    
    always @(*) begin
        npc_difftest(start_difftest);
    end
endmodule