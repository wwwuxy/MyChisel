file://<WORKSPACE>/src/main/scala/npc/controller.scala
### scala.reflect.internal.Types$TypeError: illegal cyclic reference involving object Builder

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.12
Classpath:
<WORKSPACE>/src/main/resources [exists ], <WORKSPACE>/.bloop/root/bloop-bsp-clients-classes/classes-Metals-ElXKIVHZR4KP70sESud9UA== [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/com/sourcegraph/semanticdb-javac/0.10.0/semanticdb-javac-0.10.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/chipsalliance/chisel_2.13/6.2.0/chisel_2.13-6.2.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-reflect/2.13.12/scala-reflect-2.13.12.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/github/scopt/scopt_2.13/4.1.0/scopt_2.13-4.1.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/net/jcazevedo/moultingyaml_2.13/0.4.2/moultingyaml_2.13-0.4.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/json4s/json4s-native_2.13/4.0.6/json4s-native_2.13-4.0.6.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/apache/commons/commons-text/1.10.0/commons-text-1.10.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/alexarchambault/data-class_2.13/0.2.6/data-class_2.13-0.2.6.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lihaoyi/os-lib_2.13/0.9.2/os-lib_2.13-0.9.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-parallel-collections_2.13/1.0.4/scala-parallel-collections_2.13-1.0.4.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lihaoyi/upickle_2.13/3.1.0/upickle_2.13-3.1.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/chipsalliance/firtool-resolver_2.13/1.3.0/firtool-resolver_2.13-1.3.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/github/nscala-time/nscala-time_2.13/2.22.0/nscala-time_2.13-2.22.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/yaml/snakeyaml/1.26/snakeyaml-1.26.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/json4s/json4s-core_2.13/4.0.6/json4s-core_2.13-4.0.6.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/json4s/json4s-native-core_2.13/4.0.6/json4s-native-core_2.13-4.0.6.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lihaoyi/geny_2.13/1.0.0/geny_2.13-1.0.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lihaoyi/ujson_2.13/3.1.0/ujson_2.13-3.1.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lihaoyi/upack_2.13/3.1.0/upack_2.13-3.1.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lihaoyi/upickle-implicits_2.13/3.1.0/upickle-implicits_2.13-3.1.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/dev/dirs/directories/26/directories-26.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/outr/scribe_2.13/3.13.0/scribe_2.13-3.13.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/get-coursier/coursier_2.13/2.1.8/coursier_2.13-2.1.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/joda-time/joda-time/2.10.1/joda-time-2.10.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/joda/joda-convert/2.2.0/joda-convert-2.2.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/json4s/json4s-ast_2.13/4.0.6/json4s-ast_2.13-4.0.6.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/json4s/json4s-scalap_2.13/4.0.6/json4s-scalap_2.13-4.0.6.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/thoughtworks/paranamer/paranamer/2.8/paranamer-2.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lihaoyi/upickle-core_2.13/3.1.0/upickle-core_2.13-3.1.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/outr/perfolation_2.13/1.2.9/perfolation_2.13-1.2.9.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lihaoyi/sourcecode_2.13/0.3.1/sourcecode_2.13-0.3.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-collection-compat_2.13/2.11.0/scala-collection-compat_2.13-2.11.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/outr/moduload_2.13/1.1.7/moduload_2.13-1.1.7.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/github/plokhotnyuk/jsoniter-scala/jsoniter-scala-core_2.13/2.13.5.2/jsoniter-scala-core_2.13-2.13.5.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/get-coursier/coursier-core_2.13/2.1.8/coursier-core_2.13-2.1.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/get-coursier/coursier-cache_2.13/2.1.8/coursier-cache_2.13-2.1.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/get-coursier/coursier-proxy-setup/2.1.8/coursier-proxy-setup-2.1.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/alexarchambault/concurrent-reference-hash-map/1.1.0/concurrent-reference-hash-map-1.1.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-xml_2.13/2.2.0/scala-xml_2.13-2.2.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/get-coursier/coursier-util_2.13/2.1.8/coursier-util_2.13-2.1.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/get-coursier/jniutils/windows-jni-utils/0.3.3/windows-jni-utils-0.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/codehaus/plexus/plexus-archiver/4.9.0/plexus-archiver-4.9.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/codehaus/plexus/plexus-container-default/2.1.1/plexus-container-default-2.1.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/virtuslab/scala-cli/config_2.13/0.2.1/config_2.13-0.2.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/alexarchambault/windows-ansi/windows-ansi/0.0.5/windows-ansi-0.0.5.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/javax/inject/javax.inject/1/javax.inject-1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/4.0.0/plexus-utils-4.0.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/codehaus/plexus/plexus-io/3.4.1/plexus-io-3.4.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/commons-io/commons-io/2.15.0/commons-io-2.15.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/apache/commons/commons-compress/1.24.0/commons-compress-1.24.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/iq80/snappy/snappy/0.4/snappy-0.4.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tukaani/xz/1.9/xz-1.9.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/github/luben/zstd-jni/1.5.5-10/zstd-jni-1.5.5-10.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/codehaus/plexus/plexus-classworlds/2.6.0/plexus-classworlds-2.6.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/apache/xbean/xbean-reflect/3.7/xbean-reflect-3.7.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/fusesource/jansi/jansi/1.18/jansi-1.18.jar [exists ]
Options:
-language:reflectiveCalls -deprecation -feature -Xcheckinit -Ymacro-annotations -Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 816
uri: file://<WORKSPACE>/src/main/scala/npc/controller.scala
text:
```scala
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
        val is_ecall = Output(Bool())
        val is_csr = Output(Bool())
        // val nemutrap = Output(Bool())
    })

//inital enable signal
    i@@
    io.is_ecall := false.B
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
    val is_csr = (opcode === "b1110011".U)  //csr指令相关

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
    when(is_csr){
        io.imm := Cat(Fill(27, 0.U), io.inst(19, 15))
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
//ecall
    when(io.inst === "h00000073".U){
        io.is_ecall :=true.B
        io.alu_a_sel :=true.B
        io.alu_b_sel := true.B
        io.alu_sel := "b000_00010_00000".U
        io.jump_no_jalr := true.B
    }
//csrrw、csrrs、csrrc、csrrwi、csrrsi、csrrci
}

```



#### Error stacktrace:

```

```
#### Short summary: 

scala.reflect.internal.Types$TypeError: illegal cyclic reference involving object Builder