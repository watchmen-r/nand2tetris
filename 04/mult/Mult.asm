// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.

// ＠valueのvalueは特定の値、もしくは数値を表すシンボル。Aレジスタに格納する
// MはMemory[Aレジスタに格納された値]。今回はMemory[R2]に0を入れている
@R2
M=0

// R1が0の時、LOOPが走らないようENDにジャンプする
@R1
D=M
@END
D;JLE

// (xxxは次のコマンドのアドレスを表すシンボル。どこからでも参照できる)
(LOOP)
    @R0
    D=M

    @R2
    M=M+D // R2 = R2 + R0

    @R1
    D=M-1 // R1をデクリメントする
    M=D

    @LOOP
    D;JGT // R1 > 0の時、LOOPに移動する

(END)
    @END
    0;JMP // 無限ループは、Hackプログラムを終了させる
