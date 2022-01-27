import java.util.Random;

public class NewPipe {
    Random random = new Random();
    int[] pipeBound = new int[2];
    public NewPipe(int realHeight, int score){
        int pipeY = 100+random.nextInt(realHeight-150);
        int gap = 75-(score/2);
        pipeBound[0] = pipeY-(random.nextInt(gap)+gap);
        pipeBound[1] =  pipeY+(random.nextInt(gap)+gap);
    }
}
