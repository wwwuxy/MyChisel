module Inst_Memory(
    input clk,
    input [31:0] pc,
    output [31:0] inst,
    input arvalid,
    output arready,
    output reg rresp,
    output reg rvalid,
    input rready,
    input [31:0] awaddr,
    input  awvalid,
    output awready,
    input [31:0] wdata,
    input [31:0] wstrb,
    input  wvalid,
    output  wready,
    output bresp,
    output bvalid,
    input  bready
);

    import "DPI-C" function int mem_read(input int pc, int len);

    reg [31:0] inst_delay;

    assign arready = 1;
    always @(posedge clk) begin
        // if(arvalid && !arready) begin   
        //     arready <= 1;
        // end

        if(arvalid && arready) begin
            // arready <= 0;
            if(rready && !rvalid) begin  
                rresp <= 1;
                rvalid <= 1;
                inst_delay <= mem_read(pc, 4);
                // $display("Read data from memory: 0x%h\n", mem_read(pc, 4));
            end
        end

        if(rvalid && rready) begin     
            rvalid <= 0;
            rresp <= 0;
        end
        // $display("rresp = %d\t", rresp);
        // $display("arvalid = %d\t", arvalid);
        // $display("arready = %d\t", arready);
        // $display("rvalid = %d\t", rvalid);
        // $display("rready = %d\t", rready);
        // $display("pc = 0x%h\t", pc);
        // $display("inst = 0x%h\n", inst);
    end
    
    assign inst = inst_delay;

    assign awready = 0;
    assign bresp = 0;
    assign bvalid = 0;
    
endmodule