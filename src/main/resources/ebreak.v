module is_ebreak(
    input flag,
    output nemu_trap
);
assign nemu_trap = flag;

//通过DPI-C通知仿真环境是否为ebreak
import "DPI-C" funtion bool is_break(input bool flag);

endmodule