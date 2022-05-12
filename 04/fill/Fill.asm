// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.
(INIT)
    @8192 // 32 * 256の16bitのピクセルがあるため、それをカバーする値
    D=A
    @R0
    M=D

(LOOP)
    // イメージ的には１行のさらにその中の16bitずつ色を塗っているイメージ。
    // そのため、index < 0　になったらINITしないといけない
    @R0
    M=M-1
    D=M
    @INIT
    D;JLT // index < 0の時、リセットするため(INIT)に飛ばす

    // KBDは定義済みシンボル。キーボードのメモリ位置を指す
    @KBD
    D=M;
    
    // キーボードに何か押されていたら黒に移動
    @BLACK
    D;JGT
    
    // 何も押されていなければ白に移動
    @WHITE
    0;JMP

(BLACK)
    // SCREENは定義済みシンボル。キーボードのメモリ位置を指す
    @SCREEN
    D=A
    
    @R0
    // Screenのメモリアドレス+8192をすることで、全ピクセルをカバーする
    A=D+M
    // スクリーンを黒にするには全部を1にする。
    // そのための値を設定 -1 = (1111111111111111)
    M=-1

    @LOOP // 無限ループさせるため、上の(LOOP)に戻す
    0;JMP

(WHITE)
    // SCREENは定義済みシンボル。キーボードのメモリ位置を指す
    @SCREEN
    D=A
    
    @R0
    // Screenのメモリアドレス+8192をすることで、全ピクセルをカバーする
    A=D+M
    // スクリーンを白にするには全部0にする
    // そのための値を設定 0 = (0000000000000000)
    M=0

    @LOOP // 無限ループさせるため、上の(LOOP)に戻す
    0;JMP
