package lib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by tales on 17/02/16.
 */
public class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String event = e.getActionCommand().toString();
        switch (event){
            case "Aferir":
                this.aferir();
                break;
            case "Limpar":
                this.apagar();
                break;
            case "Cancelar":
                this.cancelar();
                break;
            default:
                this.aferir();
        }
        System.out.println(e);
    }

    public void aferir(){
        System.out.println("Aferir");
    }

    public void apagar(){
        System.out.println("apagar");
    }
    public void cancelar(){
        System.out.println("cancelar");
    }

    //Apenas para verificar se a biblioteca de testes estava corretamente configurada
    public int getNumber(){
        return 0;
    }
}
