package npc

import chisel3._
import chisel3.util._

class controller extends Module{
    val io = IO(new Bundle {
        val inst = Input(UInt(32.W))
        val rf_wr_en = Output(Bool())
        val rf_wr_sel = Output(UInt(3.W))
        val alu_a_sel = Output(Bool())
        val alu_b_sel = Output(Bool())
        val mem_wr = Output(Bool())
        val alu_sel = Output(UInt(13.W))
        val jump_en = Output(Bool())
        val imm = Output(UInt(32.W))
        // val nemutrap = Output(Bool())
    })

//inital enable signal
    io.rf_wr_en := false.B
    io.jump_en := false.B
    io.alu_sel := 0.U
    io.rf_wr_sel := 0.U
    io.imm := 0.U
    io.alu_a_sel := false.B
    io.alu_b_sel := false.B
    io.mem_wr := false.B
    io.alu_sel := 0.U
    // io.nemutrap := false.B

//根据opcode确定指令类型
    val opcode = Wire(UInt(7.W))
    opcode := io.inst(6, 0)
    val isR_type = (opcode === "b0110011".U)
    val isI_type = (opcode === "b0010011".U)
    val isS_type = (opcode === "b0100011".U)
    val isB_type = (opcode === "b1100011".U)

//提取fun3字段，确定指令
    val fun3 = Wire(UInt(3.W))
    fun3 := io.inst(14, 12)

//auipc、lui、jalr和jal指令通过opcode进行区分
    val is_auipc = (opcode === "b0010111".U)
    val is_lui = (opcode === "b0110111".U)
    val is_jal = (opcode === "b1101111".U)
    val is_jalr = (opcode === "b1100111".U)

//根据指令类型提取imm
    when(isI_type){
        val imm_i = Wire(UInt(12.W))
        imm_i := io.inst(31, 20)
        io.imm := Cat(Fill(20, imm_i(11)), imm_i)
    }
    when(is_auipc){
        val imm_auipc = Wire(UInt(20.W))
        imm_auipc := io.inst(31, 12)
        io.imm := (Cat(Fill(12, imm_auipc(19)), imm_auipc)) << 12
    }
    when(is_lui){
        val imm_lui = Wire(UInt(20.W))
        imm_lui := io.inst(31, 12)
        io.imm := (Cat(Fill(12, imm_lui(19)), imm_lui)) << 12
    }
    when(is_jal){
        io.imm := (Cat(Fill(12, io.inst(31)), io.inst(19, 12), io.inst(20), io.inst(30, 21), 0.U(1.W)))
    }

//通过fun3字段进行区分不同指令
    val is_addi = (fun3 === "b000".U)
    val is_sw = (fun3 === "b010".U)

//lui
    when(is_lui){
        io.alu_sel := "b00100_0000".U
        io.alu_a_sel := false.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b100".U
    }
// auipc
    when(is_auipc){
        io.alu_sel := "b00000_0001".U
        io.alu_a_sel := false.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
//jal
    when(is_jal){
        io.jump_en := true.B
        io.alu_sel := "b00000_0001".U
        io.alu_a_sel := false.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b100".U
    }
//jalr
    when(is_jalr){
        io.jump_en := true.B
        io.alu_sel := "b10000_0000".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b100".U
    }
// addi
    when(isI_type && is_addi){
        io.alu_sel := "b00000_0001".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := "b001".U
    }
// sw
    when(isS_type && is_sw){
        //TO DO
    }
}
