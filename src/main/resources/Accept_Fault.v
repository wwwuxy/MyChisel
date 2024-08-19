module accept_fault(
    input logic is_accept_fault
);
    import "DPI-C" function void npc_accept_fault(input bit flag);
    
    always @(*) begin
        npc_accept_fault(is_accept_fault);
    end
    
endmodule