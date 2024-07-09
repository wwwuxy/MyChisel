package npc

import chisel3._
import chisel3.util._

class controller extends Module{
    val io = IO(new Bundle {
        val inst = Input(UInt(32.W))
        val alu_out = Input(UInt(32.W))
        val rf_wr_en = Output(Bool())
        val rf_wr_sel = Output(UInt(3.W))
        val alu_a_sel = Output(Bool())
        val alu_b_sel = Output(Bool())
        val mem_wr_en = Output(Bool())
        val mem_rd_en = Output(Bool())
        val alu_sel = Output(UInt(13.W))
        val jump_no_jalr = Output(Bool())
        val jump_jalr = Output(Bool())
        val len = Output(UInt(32.W))
        val imm = Output(UInt(32.W))
        val load_unsign = Output(Bool())
        // val nemutrap = Output(Bool())
    })

//inital enable signal
    io.load_unsign := false.B
    io.len := 4.U
    io.rf_wr_en := false.B
    io.jump_no_jalr := false.B
    io.jump_jalr := false.B
    io.alu_sel := 0.U
    io.rf_wr_sel := 0.U
    io.imm := 0.U
    io.alu_a_sel := false.B
    io.alu_b_sel := false.B
    io.mem_wr_en := false.B
    io.mem_rd_en := false.B
    io.alu_sel := 0.U
    // io.nemutrap := false.B

//根据opcode确定指令类型
    val opcode = Wire(UInt(7.W))
    opcode := io.inst(6, 0)
    val isR_type = (opcode === "b0110011".U)    //注意R型指令有fun7字段
    val isI_type = (opcode === "b0010011".U)
    val isS_type = (opcode === "b0100011".U)
    val isB_type = (opcode === "b1100011".U)
    val is_load =  (opcode === "b0000011".U)

//提取fun3字段，确定指令
    val fun3 = Wire(UInt(3.W))
    val fun7 = Wire(UInt(7.W))
    fun3 := io.inst(14, 12)
    fun7 := io.inst(31, 25)


//auipc、lui、jalr、jal指令通过opcode进行区分
    val is_auipc = (opcode === "b0010111".U)
    val is_lui = (opcode === "b0110111".U)
    val is_jal = (opcode === "b1101111".U)
    val is_jalr = (opcode === "b1100111".U)

//根据指令类型提取imm
    when(isI_type && (fun3 =/= "b101".U) && (fun3 =/= "b001".U)){
        io.imm := Cat(Fill(20, io.inst(31)), io.inst(31, 20))   
    }
    when(isI_type && ((fun3 === "b101".U) || (fun3 === "b001".U))){
        io.imm := Cat(0.U(27.W), io.inst(24, 20))
    }
    when(is_auipc){
        io.imm := (Cat(Fill(12, io.inst(31)), io.inst(31,12))) << 12
    }
    when(is_lui){
        val imm_lui = Wire(UInt(32.W))
        imm_lui := (Cat(Fill(12, io.inst(31)), io.inst(31,12))) << 12
        io.imm := Cat(imm_lui(31, 12), 0.U(12.W))
    }
    when(is_jal){
        io.imm := (Cat(Fill(12, io.inst(31)), io.inst(19, 12), io.inst(20), io.inst(30, 21), 0.U(1.W)))
    }
    when(is_jalr){
        io.imm := Cat(Fill(20, io.inst(31)), io.inst(31, 20))
    }
    when(is_load){          //符号扩展
        io.imm := Cat(Fill(20, io.inst(31)), io.inst(31, 20))
    }
    when(isS_type){
        io.imm := Cat(Fill(20, io.inst(31)), io.inst(31, 25), io.inst(11, 7))
    }
    when(isB_type){
        io.imm := Cat(Fill(20, io.inst(31)), io.inst(7), io.inst(30, 25), io.inst(11, 8), 0.U(1.W))
    }

//根据指令类型确定操作

//lui
    when(is_lui){
        io.alu_sel := "b000_00010_00000".U
        io.alu_a_sel := false.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
// auipc
    when(is_auipc){
        io.alu_sel := "b000_00000_00001".U
        io.alu_a_sel := false.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
//jal
    when(is_jal){
        io.jump_no_jalr := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b100".U
    }
//jalr
    when(is_jalr){
        io.jump_jalr := true.B
        io.alu_sel := "b000_00000_00001".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b100".U
    }
//beq
    when(isB_type && (fun3 === "b000".U)){
        io.alu_sel := "b100_00000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.jump_no_jalr := (io.alu_out === 1.U)
    }
//bne
    when(isB_type && (fun3 === "b001".U)){
        io.alu_sel := "b100_00000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.jump_no_jalr := (io.alu_out === 0.U)
    }
//blt
    when(isB_type && (fun3 === "b100".U)){
        io.alu_sel := "b010_00000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.jump_no_jalr := (io.alu_out === 1.U)
    }
//bge
    when(isB_type && (fun3 === "b101".U)){
        io.alu_sel := "b010_00000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.jump_no_jalr := (io.alu_out === 0.U)
    }
//btlu
    when(isB_type && (fun3 === "b110".U)){
        io.alu_sel := "b001_00000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.jump_no_jalr := (io.alu_out === 1.U)
    }
//bgeu
    when(isB_type && (fun3 === "b111".U)){
        io.alu_sel := "b001_00000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.jump_no_jalr := (io.alu_out === 0.U)
    }
//lh
    when(is_load && (fun3 === "b001".U)){
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.alu_sel := "b000_00000_00001".U
        io.len := 2.U
        io.mem_rd_en := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//lw
    when(is_load && (fun3 === "b010".U)){
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.alu_sel := "b000_00000_00001".U
        io.len := 4.U
        io.mem_rd_en := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//lbu 
    when(is_load && (fun3 === "b100".U)){
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.alu_sel := "b000_00000_00001".U
        io.load_unsign := true.B
        io.len := 1.U
        io.mem_rd_en := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//lhu
    when(is_load && (fun3 === "b101".U)){
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.alu_sel := "b000_00000_00001".U
        io.len := 2.U
        io.load_unsign := true.B
        io.mem_rd_en := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//sb
    when(isS_type && (fun3 === "b000".U)){
        io.alu_sel := "b000_00000_00001".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.len := 1.U
        io.mem_wr_en := true.B
    }
//sh
    when(isS_type && (fun3 === "b001".U)){
        io.alu_sel := "b000_00000_00001".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.len := 2.U
        io.mem_wr_en := true.B
    }
// sw
    when(isS_type && (fun3 === "b010".U)){
        io.alu_sel := "b000_00000_00001".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.len := 4.U
        io.mem_wr_en := true.B
   }
//addi
    when(isI_type && (fun3 === "b000".U)){
        io.alu_sel := "b000_00000_00001".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
//sltiu
    when(isI_type && (fun3 === "b011".U)){
        io.alu_sel := "b001_00000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
//xori
    when(isI_type && (fun3 === "b100".U)){
        io.alu_sel := "b000_10000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
//andi
   when(isI_type && (fun3 === "b111".U)){
        io.alu_sel := "b000_00100_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
//slli
    when(isI_type && (fun3 === "b001".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b000_00000_00100".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
//srli
    when(isI_type && (fun3 === "b101".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b000_00000_01000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
//srai
    when(isI_type && (fun3 === "b101".U) && (fun7 === "b0100000".U)){
        io.alu_sel := "b000_00000_10000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }

//add 
    when(isR_type && (fun3 === "b000".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b000_00000_00001".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//sub
   when(isR_type && (fun3 === "b000".U) && (fun7 === "b0100000".U)){
        io.alu_sel := "b000_00000_00010".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//sll
    when(isR_type && (fun3 === "b001".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b000_00000_00100".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//slt
    when(isR_type && (fun3 === "b010".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b010_00000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//sltu
    when(isR_type && (fun3 === "b011".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b001_00000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//xor
    when(isR_type && (fun3 === "b100".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b000_10000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//srl
    when(isR_type && (fun3 === "b101".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b000_00000_01000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//sra
    when(isR_type && (fun3 === "b101".U) && (fun7 === "b0100000".U)){
        io.alu_sel := "b000_00000_10000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//or
    when(isR_type && (fun3 === "b110".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b000_01000_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
//and 
    when(isR_type && (fun3 === "b111".U) && (fun7 === "b0000000".U)){
        io.alu_sel := "b000_00100_00000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := true.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b010".U
    }
}
