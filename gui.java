import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

// used https://stackabuse.com/java-check-if-array-contains-value-or-element/
// used https://stackoverflow.com/questions/5929202/change-font-color-of-textfield-input-via-jcheckbox

public class gui extends JFrame implements ActionListener {
    JTextField textField = new JTextField();
    JButton eq, dot, plus, min, div, mul, del, clear, neg, rad, factor, log, pow;
    JButton[] numButtons, funcButtons;
    double result = 0;
    boolean dotUsed = false, negUsed = false;
    char op = '#';
    public gui() {
        //create frame
        JFrame f = new JFrame("Calculator");
        setFrame(f);
        // create text field:
        int fieldwidth = 380;
        textField.setBounds(10, 20, fieldwidth, 50);
        textField.setEditable(false);
        // create number button panel:
        JPanel numbersPanel = new JPanel();
        numbersPanel.setBounds(textField.getX(), textField.getY() + textField.getHeight() + 20, fieldwidth * 3 / 4, fieldwidth + 4);
        //   ^^^ due to some calculations height must be bigger than (3/4)(width) .. the most simple way is adding a value like +4 to (3/4)(width)
        numButtons = numButtonsCreator();
        funcButtons = funcButtonsCreator();
        int spaceBetweenButtons;
/*
        if we want to create a 4*3 grid layout with square buttons then width must be equal to:
        3*buttonSize + 2*space
        and height must be equal to:
        4*buttonSize + 3*space
        so:
 */
        spaceBetweenButtons = 3 * numbersPanel.getHeight() - 4 * numbersPanel.getWidth() ;
        numbersPanel.setLayout(new GridLayout(4, 3, spaceBetweenButtons, spaceBetweenButtons));
        for (int i = 1; i < 10; i++) numbersPanel.add(numButtons[i]);
        numbersPanel.add(dot);
        numbersPanel.add(numButtons[0]);
        numbersPanel.add(eq);
        // create func button panel:
        JPanel funcPanel = new JPanel();
        funcPanel.setBounds(numbersPanel.getX() + numbersPanel.getWidth() + spaceBetweenButtons, numbersPanel.getY(),
                fieldwidth * 1 / 4 - spaceBetweenButtons, numbersPanel.getHeight());
        funcPanel.setLayout(new GridLayout(4, 1, 0, spaceBetweenButtons));
        //add func buttons:
        funcPanel.add(plus);
        funcPanel.add(min);
        funcPanel.add(mul);
        funcPanel.add(div);
        //create del,(-),c panel
        JPanel editPanel = new JPanel();
        editPanel.setBounds(numbersPanel.getX(),numbersPanel.getY()+numbersPanel.getHeight()+spaceBetweenButtons,
                textField.getWidth(),numbersPanel.getHeight()/4);
        editPanel.setLayout(new GridLayout(2, 4, spaceBetweenButtons, spaceBetweenButtons));
        editPanel.add(clear);
        editPanel.add(del);
        editPanel.add(neg);
        editPanel.add(pow);
        editPanel.add(factor);
        editPanel.add(rad);
        editPanel.add(log);
        //set font
        setFonts();
        //add panels and text field
        f.add(editPanel);
        f.add(funcPanel);
        f.add(numbersPanel);
        f.add(textField);
        f.setVisible(true);
    }
    public static void main(String[] args) {
        gui f = new gui();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Integer index = Arrays.asList(numButtons).indexOf(e.getSource());
        if (index >= 0) addText(index.toString());//if it's number
        else if(e.getSource() == factor)textField.setText(fac(Double.parseDouble(textField.getText())).toString());
        else if (e.getSource() == clear){
            result = 0;
            textField.setText("");
        }
        else if (e.getSource() == pow)changeResult('^');
        else if (e.getSource() == rad)textField.setText(String.valueOf(Math.sqrt(Double.parseDouble(textField.getText()))));
        else if (e.getSource() == log)textField.setText(String.valueOf(Math.log10(Double.parseDouble(textField.getText()))));
        else if (e.getSource() == del) textField.setText(textField.getText().substring(0,textField.getText().length()-1));
        else if (e.getSource() == neg) {
            if(!negUsed){
                textField.setText("-" + textField.getText());
                negUsed = true;
            }
            else {
                textField.setText(textField.getText().substring(1));
                negUsed = false;
            }
        } else if (e.getSource() == dot && !dotUsed) {
            addText(".");
            dotUsed = true;
        } else {     //  << + - * / = >>
            if (e.getSource() == eq) {
                if(op == '*' && Double.parseDouble(textField.getText()) == 0)textField.setText("Invalid /0");
                else{
                    changeResult(op);
                    textField.setText(Double.toString(result));
                }
            }else changeResult(e.getSource().toString().charAt(e.getSource().toString().lastIndexOf("text=") + 5));//what is the name of button
        }
    }

    ///       functions:

    public void setFrame(JFrame f) {
        f.setSize(420, 640);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);
    }
    public void createButton() {
        eq = new JButton("=");
        dot = new JButton(".");
        plus = new JButton("+");
        min = new JButton("-");
        div = new JButton("/");
        mul = new JButton("*");
        del = new JButton("DEL");
        neg = new JButton("(-)");
        clear = new JButton("CLR");
        pow = new JButton("X^y");
        factor = new JButton("X!");
        log = new JButton("log10");
        rad = new JButton("radical");
    }
    public JButton[] numButtonsCreator() {
        JButton[] buttons = new JButton[10];

        for (int i = 0; i < 10; i++) {
            buttons[i] = new JButton(String.valueOf(i));
            buttons[i].addActionListener(this::actionPerformed);
            buttons[i].setFocusable(false);
        }
        return buttons;
    }
    public JButton[] funcButtonsCreator() {
        JButton[] buttons = new JButton[13];
        createButton();
        buttons[0] = clear;
        buttons[1] = del;
        buttons[2] = neg;
        buttons[3] = dot;
        buttons[4] = eq;
        buttons[5] = plus;
        buttons[6] = min;
        buttons[7] = mul;
        buttons[8] = div;
        buttons[9] = rad;
        buttons[10] = factor;
        buttons[11] = log;
        buttons[12] = pow;
        for (JButton b : buttons){
            b.addActionListener(this::actionPerformed);
            b.setFocusable(false);
        }
        return buttons;
    }
    public void addText(String inp) {
        textField.setText(textField.getText() + inp);
    }
    public void changeResult (char operatorChar) {
        switch (op) {
            default:
                dotUsed = false;
            case '#':
            case '+':
                result += Double.parseDouble(textField.getText());
                op = operatorChar;
                System.out.println(result);
                textField.setText("");
                break;
            case '-':
                result -= Double.parseDouble(textField.getText());
                op = operatorChar;
                textField.setText("");
                break;
            case '/':
                result *= Double.parseDouble(textField.getText());
                op = operatorChar;
                textField.setText("");
                break;
            case '*':

                if (Double.parseDouble(textField.getText()) != 0) {
                result /= Double.parseDouble(textField.getText());
                op = operatorChar;
                textField.setText("");
            }
            break;
            case '^':
                result = Math.pow(result,Double.parseDouble(textField.getText()));
                System.out.println(result);
                op = operatorChar;
                textField.setText("");
        }
    }
    public void setFonts(){
        Font num = new Font("Dialog",Font.BOLD,30);
        Font func = new Font("Dialog",Font.BOLD,20);
        Color text= new Color(0x2E5EAA);
        textField.setFont(func);
        textField.setForeground(text);
        for(JButton b : numButtons){
            b.setFont(num);
            b.setForeground(text);
        }
        for (JButton b : funcButtons){
            b.setForeground(text);
            b.setFont(func);
        }
    }
    Double fac(double inp){
        System.out.println(inp + "*");
        if (inp == 0) return 1.0;
        //else return inp*(inp-1);
        else return inp*fac(inp-1);
    }
}
