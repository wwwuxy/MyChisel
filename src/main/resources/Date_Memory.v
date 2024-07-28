module Date_Memory(
    input clk,
    input arvalid,
    input [31:0] araddr,
    input load_unsign,
    output arready,

    output reg [31:0] rdata,
    output reg rresp,
    output reg rvalid,
    input rready,

    input awvalid,
    input [31:0] awaddr,
    output reg awready,

    input wvalid,
    input [31:0] wdata,
    input [31:0] len,
    output reg wready,

    output reg bresp,
    output reg bvalid,
    input bready
);

    import "DPI-C" function int mem_read(input int addr, int len);
    import "DPI-C" function void mem_write(input int addr, int len, input int data);

    reg [31:0] data_delay;  //模拟访存延迟
    assign rdata = data_delay;

    assign arready = 1;     //ready都是1

    always @(posedge clk) begin
        reg [31:0] temp_data;

        if (arvalid && arready) begin
            if (rready && !rvalid) begin    //读握手成功
            $display("araddr = 0x%h\t", araddr);
                rresp <= 1;
                rvalid <= 1;
                if(load_unsign) begin
                    if(len == 1) begin
                        data_delay <= {24'b0, mem_read(araddr, 1)};
                    end
                    else if(len == 2) begin
                        data_delay <= {16'b0, mem_read(araddr, 2)};
                    end
                    else if(len == 4) begin
                        data_delay <= mem_read(araddr, 4);
                    end
                end
                else begin
                    temp_data = mem_read(araddr, len);
                    if(len == 1) begin
                        data_delay <= {{24{temp_data[7]}}, temp_data[7:0]};
                    end
                    else if(len == 2) begin
                        data_delay <= {{16{temp_data[15]}}, temp_data[15:0]};
                    end
                    else if(len == 4) begin
                        data_delay <= mem_read(araddr, 4);
                    end
                end
            end
        end
        
//写        
        if (awvalid && !awready) begin
            awready <= 1;
        end     
        
        if (awvalid && awready) begin
            awready <= 0;
        end

        if (wvalid && !wready) begin  //写握手
                wready <= 1;
                mem_write(awaddr, len, wdata);
                bvalid <= 1;
            end

        if(rvalid && rready) begin  //读取成功,清除信号
            rvalid <= 0;
            rresp <= 0;
        end

        if(bvalid && bready) begin  //写入成功,清除信号
            wready <= 0;
            bresp <= 1;
            bvalid <= 0;
        end

        if(bresp) begin
            bresp <= 0;
        end
        // $display("arvalid = %d\t", arvalid);
        // $display("arready = %d\t", arready);
        // $display("rvalid = %d\t", rvalid);
        // $display("rready = %d\t", rready);
        // $display("rresp = %d\t", rresp);
    end


endmodule
