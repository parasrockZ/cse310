import java.sql.*;
import java.util.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

class Global{
    public static String currentUser;
    public static int login;
    public static int cID;
    public static Connection con;
    static void getCid(){
    try{
		File f1 = new File("/home/rex100/Codes/JAVAproject/cid.txt");
		Scanner sc = new Scanner(f1);
		cID = Integer.parseInt(sc.nextLine());
        sc.close();
    }
	    catch(FileNotFoundException e){
	        e.printStackTrace();
	    }

    }
    static void setCid(){
        try{
        FileWriter f1 = new FileWriter("/home/rex100/Codes/JAVAproject/cid.txt");
        try{
        String s = Integer.toString(++cID);
        f1.write(s);
        f1.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        }
        catch (IOException e){
            System.out.println("ERROR33");
        }
    }
}

public class DBpractice {
    DBpractice(){
    try{
    Global.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/RMART","root","1234");
    }
    catch(SQLException e){
        e.printStackTrace();
    }
}
    static int checkUser(String empid,String pass){
        String query = String.format("SELECT * FROM EmpTable WHERE EmpId = %d AND Pass = '%s'",Integer.parseInt(empid),pass);
        try{
            Statement p = Global.con.createStatement();
            ResultSet s1 = p.executeQuery(query);
            if(s1.next()){
                Global.currentUser = empid;
                return 1;
            }
        }
        catch(SQLException e){
            System.out.println("EXCEPTION");
            return 0;
        }
        return 0;
    }
    static int checkCust(String name,String phno){
        String query;
        if(name == "-1")
        query = String.format("SELECT * FROM CustomerTable WHERE PhoneNumber = '%s'",phno);
        else
        query = String.format("SELECT * FROM CustomerTable WHERE Name = '%s' AND PhoneNumber = '%s'",name,phno);
        try{
            Statement p = Global.con.createStatement();
            ResultSet s1 = p.executeQuery(query);
            if(s1.next()){
                return 1;
            }
        }
        catch(SQLException e){
            return 0;
        }
        return 0;
    }
    static int checkArt(int an){
        String query = String.format("SELECT * FROM InvTable WHERE ArticleNo = %d",an);
        try{
            Statement p = Global.con.createStatement();
            ResultSet s1 = p.executeQuery(query);
            if(s1.next()){
                return 1;
            }
        }
        catch(SQLException e){
            return 0;
        }
        return 0;
    }
    static void addData(int n,String... args){
        if(n == 1){
            int t = 0;
            String query = String.format("INSERT INTO CustomerTable VALUES(%d,'%s','%s','%s','%s')",++Global.cID,args[0],args[1],args[2],args[3]);
            try{
                Statement p = Global.con.createStatement();
                t = p.executeUpdate(query);
                Global.setCid();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            if(t != 1)
                notice.warn(-3);
            else
                notice.warn(3);
        }
        else if(n == 2){
            int t = 0;
            String query = String.format("INSERT INTO InvTable VALUES(%d,'%s',%f,%d)",Integer.parseInt(args[0]),args[1],Float.parseFloat(args[2]),Integer.parseInt(args[3]));
            try{
                Statement p = Global.con.createStatement();
                t = p.executeUpdate(query);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            if(t != 1)
                notice.warn(0);
            else
                notice.warn(5);
        }
}
    static void delRecord(int n,String... args){
        if(n == 1){
            int t;
            String query = String.format("DELETE FROM CustomerTable WHERE Name = '%s' AND PhoneNumber = '%s'",args[0],args[1]);
            try{
                Statement p = Global.con.createStatement();
                t = p.executeUpdate(query);
            }
            catch(SQLException e){
                e.printStackTrace();
                t = 0;
            }
            if(t == 1)
                notice.warn(4);
            else
                notice.warn(0);
        }
        else if(n == 2){
            int t;
            String query = String.format("DELETE FROM InvTable WHERE ArticleNo = %d",Integer.parseInt(args[0]));
            try{
                Statement p = Global.con.createStatement();
                t = p.executeUpdate(query);
            }
            catch(SQLException e){
                e.printStackTrace();
                t = 0;
            }
            if(t == 1)
                notice.warn(-5);
            else
                notice.warn(0);
        }
    }
    public static void main(String... args){
        Global.getCid();
        new DBpractice();
        new awtP();
    }
}

class notice{
    static  void warn(int n){
            String msg = "";
            switch (n){
                case 0:
                    msg = "Please Contact Devloper!!!";
                    break;
                case -1:
                    msg = "EMPLOYEE ID OR PASSWORD IS WRONG!!";
                    break;
                case -2:
                    msg = "NO CUSTOMER FOUND";
                    break;
                case 2:
                    msg = "CUSTOMER FOUND!!!";
                    break;
                case 3:
                    msg = "CUSTOMER ADDED!!!";
                    break;
                case 4:
                    msg = "CUSTOMER REMOVED!!!";
                    break;
                case 5:
                    msg = "ARTICLE ADDED!!!";
                    break;
                case -5:
                    msg = "ARTICLE REMOVED!!!";
                    break;
                case 6:
                    msg = "ARTICLE FOUND!!!";
                    break;
                case -6:
                    msg = "ARTICLE NOT FOUND!!!";
                    break;
                case 10:
                    msg = "NOT AVAILABLE!!!";
                default:
                    msg = "ERROR"; 
            }
            Frame f1 = new Frame();
            Button b1 = new Button("OK!");
            Label t1 = new Label(msg);
            f1.setSize(500,200);
            t1.setBounds(100,100,400,20);
            t1.setFont(new Font("Comic Sans",Font.BOLD,15));
            b1.setBounds(225,150,80,30);
            b1.setBackground(new Color(50,205,20));

            f1.add(t1);
            f1.add(b1);
            f1.setLayout(null);
            f1.setVisible(true);
            

            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    f1.dispose();
                }    
            });
    }
}
class awtP extends Frame{
    awtP(){
        Frame f1 = new Frame();
        Button b1 = new Button("SUBMIT");

        Label l1 = new Label("Employee ID");
        Label l2 = new Label("Password");

        l1.setFont(new Font("Comic Sans",Font.BOLD,20));
        l2.setFont(new Font("Comic Sans",Font.BOLD,20));
        b1.setFont(new Font("Comic Sans",Font.BOLD,20));
        TextField t1 = new TextField();
        TextField t2 = new TextField();
        t1.setFont(new Font("Comic Sans",Font.PLAIN,20));
        t2.setFont(new Font("Comic Sans",Font.PLAIN,20));
        t2.setEchoChar('*');

        f1.setSize(500,500);

        l1.setBounds(100,250,150,30);
        l2.setBounds(100,300,150,30);
        t1.setBounds(250,250,150,30);
        t2.setBounds(250,300,150,30);  
        b1.setBounds(250,350,100,50);      

        f1.add(l1);
        f1.add(l2);
        f1.add(t1);
        f1.add(t2);
        f1.add(b1);
        f1.setLayout(null);
        f1.setVisible(true);
        
        f1.setBackground(new Color(197,236,235));
        b1.setBackground(new Color(144,238,144));

        b1.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                String empid = t1.getText().toString(); 
                String pass = t2.getText().toString();
                Global.login=DBpractice.checkUser(empid, pass);
                if(Global.login == 0){
                    notice.warn(-1);
                }
                else{
                    f1.dispose();
                    EmpInterface();
                }
            }});    
    }
    static void EmpInterface(){
        DateTimeFormatter t = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime n = LocalDateTime.now();
        Frame f1 = new Frame();
        f1.setSize(400,150);

        Button b1 = new Button("Inventory");
        Button b2 = new Button("Customers");
        Button b3 = new Button("Checkout");

        String forLabel = n.format(t)+" EMPLOYEE NUMBER: "+Global.currentUser;
        
        Label l1 = new Label(forLabel);
        l1.setFont(new Font("Comic Sans",Font.BOLD,15));

        f1.setBackground(new Color(255,251,206));
        
        b1.setBackground(new Color(161,255,169));
        b2.setBackground(new Color(161,255,255));
        b3.setBackground(new Color(255,76,60));

        b1.setFont(new Font("Comic Sans",Font.BOLD,20));
        b2.setFont(new Font("Comic Sans",Font.BOLD,20));
        b3.setFont(new Font("Comic Sans",Font.BOLD,20));


        f1.setLayout(new BorderLayout(0,10));
        b1.setSize(18,10);
        f1.add(l1,BorderLayout.NORTH);
        f1.add(b1,BorderLayout.WEST);
        f1.add(b2,BorderLayout.EAST);
        f1.add(b3,BorderLayout.CENTER);

        f1.setVisible(true);
        
        b1.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e){
                    f1.dispose();
                    inventory.interf();
                }
        });
        b2.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e){
                    f1.dispose();
                    customer.interf();
                }
        });
        b3.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e){
                    f1.dispose();
                    verifyCustom();
                }
        });
    }
    static void verifyCustom(){
        Frame f1 = new Frame();
        Label l1 = new Label("CUSTOMER PHONENUMBER");
        TextField t1 = new TextField();
        Button b1 = new Button("CHECK");
        Button b2 = new Button("BACK");

        l1.setFont(new Font("Comic Sans",Font.PLAIN,20));
        t1.setFont(new Font("Comic Sans",Font.PLAIN,20));
        b1.setFont(new Font("Comic Sans",Font.PLAIN,20));
        b2.setFont(new Font("Comic Sans",Font.PLAIN,20));
        
        t1.setBackground(new Color(255,253,208));
        
        b1.setBackground(new Color(173,248,2));
        b2.setBackground(new Color(211,211,211));
        l1.setBounds(30,125,320,40);
        t1.setBounds(353,125,200,30);
        b1.setBounds(177,175,100,30);
        b2.setBounds(300,175,100,30);
        f1.setBackground(new Color(255,253,208));
        f1.add(l1);
        f1.add(t1);
        f1.add(b1);
        f1.add(b2);
        f1.setSize(600,300);
        f1.setLayout(null);
        f1.setVisible(true);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                f1.dispose();
                EmpInterface();
            }
        });
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(DBpractice.checkCust("-1",t1.getText().toString()) == 1){
                    f1.dispose();
                    checkout.interf();
                }
                else{
                    notice.warn(-2);
                }
                System.out.println(t1.getText().toString());
            }
        });

    }
}
class checkout{

    static float totalCost;

    int ano,qty;
    String name;
    float price;
    static int tempSize;
    checkout(int ano,String name,float price,int qty){
        this.ano = ano;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }
    static int getObj(String a){
        String query = String.format("SELECT * FROM InvTable WHERE ArticleNo = %d",Integer.parseInt(a));
        try{
            Statement p = Global.con.createStatement();
            ResultSet s1 = p.executeQuery(query);
            if(s1.next()){
              return 1;  
            }
        }
        catch(SQLException e){
            System.out.println("EXCEPTION");
            return 0;
        }
        return 0;
    }
    // edit this
    static ArrayList<String> getData(int ano){
        ArrayList<String> obj = new ArrayList<String>();
        String query = String.format("SELECT * FROM InvTable WHERE ArticleNo = %d",ano);
        try{
            PreparedStatement p = (Global.con).prepareStatement(query);
            ResultSet s1 = p.executeQuery();
            while(s1.next()){
                obj.add(Integer.toString(s1.getInt(1)));
                obj.add(s1.getString(2));
                obj.add(Float.toString(s1.getFloat(3)));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("EXCEPTION592");
        }
        return obj;
    }
    static int getQty(int ano,int qty){
        int g = 0; 
        String query = String.format("SELECT * FROM InvTable WHERE ArticleNo = %d",ano);
        try{
            PreparedStatement p = (Global.con).prepareStatement(query);
            ResultSet s1 = p.executeQuery();
            while(s1.next()){
                g = s1.getInt(4);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("EXCEPTION592");
        }
        if(g > qty){
            query = String.format("UPDATE InvTable SET Quantity = %d WHERE ArticleNo = %d",g-qty,ano);
            try{
                Statement s1 = Global.con.createStatement();
               s1.executeUpdate(query);
            }
            catch(SQLException e){
                e.printStackTrace();
                g = -999;
            }
        }
        return g>qty?1:0;
    }
    static void setQty(int ano,int qty){
        int g = 0; 
        String query = String.format("SELECT * FROM InvTable WHERE ArticleNo = %d",ano);
        try{
            PreparedStatement p = (Global.con).prepareStatement(query);
            ResultSet s1 = p.executeQuery();
            while(s1.next()){
                g = s1.getInt(4);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("EXCEPTION592");
        }
            query = String.format("UPDATE InvTable SET Quantity = %d WHERE ArticleNo = %d",g+qty,ano);
            try{
                Statement s1 = Global.con.createStatement();
               s1.executeUpdate(query);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
    }
    static void save1func(){
        JFrame f1 = new JFrame();
        f1.setSize(500,500);
        
        JLabel paid = new JLabel("Paid");
        JTextField pd = new JTextField();
        JLabel change = new JLabel("Change");
        JLabel temp = new JLabel("");
        Button b1 = new Button("Open Cash Register");


        paid.setFont(new Font("Comic Sans",Font.BOLD,20));
        pd.setFont(new Font("Comic Sans",Font.PLAIN,20));
        b1.setFont(new Font("Comic Sans",Font.PLAIN,20));

        change.setFont(new Font("Comic Sans",Font.BOLD,20));
        temp.setFont(new Font("Comic Sans",Font.PLAIN,20));

        paid.setBackground(new Color(240,236,235));
        change.setBackground(new Color(240,236,235));
        temp.setBackground(new Color(240,236,235));
        b1.setBackground(new Color(190,190,100));

        paid.setBounds(175, 200, 70, 30);
        pd.setBounds(300, 200, 70,30);
        change.setBounds(175,250,150,30);
        temp.setBounds(300,250,70,30);
        b1.setBounds(125,300,250,40);

        f1.add(paid);
        f1.add(pd);
        f1.add(change);
        f1.add(temp);
        f1.add(b1);

        f1.setLayout(null);
        f1.setBackground(new Color(240,236,235));

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                    temp.setText(Float.toString(Float.parseFloat(pd.getText())-totalCost));
            }
        });


        f1.setVisible(true);


    }
    static ArrayList<Integer> getRandomElements(){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        String query = String.format("SELECT ArticleNo FROM InvTable");
        try{
            PreparedStatement p = (Global.con).prepareStatement(query);
            ResultSet s1 = p.executeQuery();
            while(s1.next()){
                temp.add(s1.getInt(1));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in fetching article numbers");
        }
        return temp;
    } 
    static void interf(){
    JFrame f1 = new JFrame();
    JTable tab = new JTable();
    String col[] = {"Article Number","Name","Price","Quantity"};
    DefaultTableModel dtm = new DefaultTableModel(col,0);
    ArrayList<Integer> temp = getRandomElements();
    tempSize = temp.size();
    tab.setModel(dtm);
    
    JScrollPane scrollPane = new JScrollPane(tab);
    
    tab.getTableHeader().setFont(new Font("Comic Sans",Font.PLAIN,20));
    tab.setRowHeight(30);
    scrollPane.setFont(new Font("Comic Sans",Font.PLAIN,20));
    tab.setFont(new Font("Comic Sans",Font.PLAIN,20));
    
    scrollPane.setBounds(960,100,800,800);
    
    f1.add(tab.getTableHeader());
    f1.add(scrollPane);

    JButton b1 = new JButton("ADD");
    JButton b2 = new JButton("DELETE");
    JButton b3 = new JButton("SCAN");
    JButton b4 = new JButton("BACK");
    JButton b5 = new JButton("SAVE");
    
    JLabel name = new JLabel("Name");
    JLabel Ano = new JLabel("Article Number");
    JLabel Qty = new JLabel("Quantity");
    JLabel pr = new JLabel("Price");
    JLabel nt = new JLabel();
    JLabel pt = new JLabel();
    JLabel total1 = new JLabel("TOTAL");
    JLabel total = new JLabel(Float.toString(totalCost));
    
    JTextField at = new JTextField();
    JTextField qt = new JTextField();

    name.setFont(new Font("Comic Sans",Font.BOLD,20));
    Ano.setFont(new Font("Comic Sans",Font.BOLD,20));
    Qty.setFont(new Font("Comic Sans",Font.BOLD,20));
    pr.setFont(new Font("Comic Sans",Font.BOLD,20));
    nt.setFont(new Font("Consolas",Font.PLAIN,20));
    pt.setFont(new Font("Consolas",Font.PLAIN,20));
    at.setFont(new Font("Consolas",Font.PLAIN,20));
    qt.setFont(new Font("Consolas",Font.PLAIN,20));
    total.setFont(new Font("Consolas",Font.PLAIN,30));
    total1.setFont(new Font("Consolas",Font.BOLD,30));
    
    Ano.setBounds(200,350,150,30);
    name.setBounds(200,400,300,30);
    pr.setBounds(200,450,150,30);
    Qty.setBounds(200,500,150,30);
    total1.setBounds(200,550,300,40);

    at.setBounds(400,350,200,30);
    nt.setBounds(400,400,400,30);
    pt.setBounds(400,450,200,30);
    qt.setBounds(400,500,200,30);
    total.setBounds(400,550,300,40);

    b1.setBounds(200,600,100,100);
    b2.setBounds(350,600,100,100);
    b3.setBounds(500,600,100,100);
    b4.setBounds(350,750,100,100);
    b5.setBounds(500,750,100,100);
    f1.add(name);
    f1.add(Ano);
    f1.add(nt);
    f1.add(pt);
    f1.add(at);
    f1.add(qt);
    f1.add(Qty);
    f1.add(pr);
    f1.add(b1);
    f1.add(b2);
    f1.add(b3);
    f1.add(b4);
    f1.add(b5);
    f1.add(total);
    f1.add(total1);

    b1.setBackground(new Color(50,205,50));
    b1.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b1.setBorder(null);
    b1.setFocusPainted(false);

    b2.setBackground(new Color(255,61,65));
    b2.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b2.setBorder(null);
    b2.setFocusPainted(false);

    b3.setBackground(new Color(255, 204, 203));
    b3.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b3.setBorder(null);
    b3.setFocusPainted(false);

    b4.setBackground(new Color(0,255,255));
    b4.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b4.setBorder(null);
    b4.setFocusPainted(false);

    b5.setBackground(new Color(255, 253, 208));
    b5.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b5.setBorder(null);
    b5.setFocusPainted(false);

    tab.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e){
            int i = tab.getSelectedRow();
            at.setText(dtm.getValueAt(i, 0).toString());
            nt.setText(dtm.getValueAt(i, 1).toString());
            pt.setText(dtm.getValueAt(i, 2).toString());
            qt.setText(dtm.getValueAt(i, 3).toString());
        }
    });

    b5.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
        f1.dispose();
        save1func();
        }
    });

    b3.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            int g = (int)(Math.random() * tempSize);
            String a = Integer.toString(temp.get(g));
            ArrayList<String> p = getData(Integer.parseInt(a));
            at.setText(p.get(0));
            nt.setText(p.get(1));
            pt.setText(p.get(2));
            qt.setText("1");
        }

    });
    b1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String ano1 = at.getText().toString();
            if(getObj(ano1) == 1){
            String name = nt.getText().toString();
            String price = pt.getText().toString();
            String q = qt.getText().toString();
            if(getQty(Integer.parseInt(ano1),Integer.parseInt(q)) == 1){
            Object o[] = new Object[4];
            o[0] = ano1;
            o[1] = name;
            o[2] = price;
            o[3] = q;
            dtm.addRow(o);
            nt.setText("");
            pt.setText("");
            at.setText("");
            qt.setText("");
            totalCost += Float.parseFloat(price)*(float)Integer.parseInt(q);
            total.setText(Float.toString(totalCost));
            }
            else{
                notice.warn(10);
            }
        }
        else
            notice.warn(-6);
        }
    });
    b2.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String a = at.getText().toString();
            setQty(Integer.parseInt(a),Integer.parseInt(qt.getText()));
            totalCost -= Float.parseFloat(pt.getText())*(float)Integer.parseInt(qt.getText());
            if(tab.getSelectedRow() != -1)
            dtm.removeRow(tab.getSelectedRow());
            nt.setText("");
            pt.setText("");
            at.setText("");
            qt.setText("");
            total.setText(Float.toString(totalCost));
            
        }
    });

    b4.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
                f1.dispose();
                awtP.EmpInterface();
        }
    });
    f1.getContentPane().setBackground(new Color(161,255,169));
    f1.setSize(1920,1080);
    
    f1.setLayout(null);
    f1.setVisible(true);
    }
}
class customer{
    int cid;
    String name,phno,gender,membershp;
    customer(int cid,String name,String phno,String gender,String membershp){
        this.cid = cid;
        this.name = name;
        this.phno = phno;
        this.gender = gender;
        this.membershp = membershp;
    }
    static Object[] getObj(customer c1){
        Object a[] = new Object[5];
        a[0] = c1.cid;
        a[1] = c1.name;
        a[2] = c1.phno;
        a[3] = c1.gender;
        a[4] = c1.membershp;
        return a; 
    }
    static ArrayList<customer> getData(){
        ArrayList<customer> l1 = new ArrayList<customer>();
        String query = String.format("SELECT * FROM CustomerTable");
        try{
            PreparedStatement p = (Global.con).prepareStatement(query);
            ResultSet s1 = p.executeQuery();
            while(s1.next()){
                l1.add(new customer(s1.getInt(1),s1.getString(2),s1.getString(3),s1.getString(4),s1.getString(5)));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("EXCEPTION");
        }
        return l1;
    }
    static void interf(){
    JFrame f1 = new JFrame();
    JTable tab = new JTable();
    String col[] = {"Customer ID","Name","Phone Number","Gender","Memebership"};
    DefaultTableModel dtm = new DefaultTableModel(col,0);
    ArrayList<customer> l1 = getData();
    for(customer e:l1){
       dtm.addRow(getObj(e));
    }
    tab.setModel(dtm);
    
    JScrollPane scrollPane = new JScrollPane(tab);
    
    tab.getTableHeader().setFont(new Font("Comic Sans",Font.PLAIN,20));
    tab.setRowHeight(30);
    scrollPane.setFont(new Font("Comic Sans",Font.PLAIN,20));
    tab.setFont(new Font("Comic Sans",Font.PLAIN,20));
    
    scrollPane.setBounds(960,100,800,800);
    
    f1.add(tab.getTableHeader());
    f1.add(scrollPane);
    tab.setBackground(new Color(161,255,255));

    JButton b1 = new JButton("ADD");
    JButton b2 = new JButton("DELETE");
    JButton b3 = new JButton("SEARCH");
    JButton b4 = new JButton("BACK");
    
    JLabel name = new JLabel("Name");
    JLabel phno = new JLabel("Phone Number");
    JLabel gender = new JLabel("Gender");
    JLabel membr = new JLabel("Memeber");
    
    JTextField nt = new JTextField();
    JTextField pt = new JTextField();

    JRadioButton male = new JRadioButton("Male");
    JRadioButton female = new JRadioButton("Female");
    
    JRadioButton sil = new JRadioButton("Silver");
    JRadioButton bro = new JRadioButton("Bronze");
    JRadioButton gol = new JRadioButton("Gold");

    name.setFont(new Font("Comic Sans",Font.BOLD,20));
    phno.setFont(new Font("Comic Sans",Font.BOLD,20));
    gender.setFont(new Font("Comic Sans",Font.BOLD,20));
    membr.setFont(new Font("Comic Sans",Font.BOLD,20));
    male.setFont(new Font("Comic Sans",Font.PLAIN,20));
    female.setFont(new Font("Comic Sans",Font.PLAIN,20));
    sil.setFont(new Font("Comic Sans",Font.PLAIN,20));
    bro.setFont(new Font("Comic Sans",Font.PLAIN,20));
    gol.setFont(new Font("Comic Sans",Font.PLAIN,20));
    nt.setFont(new Font("Consolas",Font.PLAIN,20));
    pt.setFont(new Font("Consolas",Font.PLAIN,20));
    
    name.setBounds(200,350,150,30);
    phno.setBounds(200,400,300,30);

    nt.setBounds(400,350,200,30);
    pt.setBounds(400,400,200,30);
    
    gender.setBounds(200,450,100,30);
    male.setBounds(300,450,100,30);
    male.setBackground(new Color(161,255,255));
    female.setBounds(400,450,100,30);
    female.setBackground(new Color(161,255,255));
    membr.setBounds(200,500,150,30);
    bro.setBounds(350,500,100,30);
    bro.setBackground(new Color(161,255,255));
    sil.setBounds(450,500,100,30);
    sil.setBackground(new Color(161,255,255));
    gol.setBounds(550,500,100,30);
    gol.setBackground(new Color(161,255,255));
    
    ButtonGroup g = new ButtonGroup();
    g.add(male);
    g.add(female);
    
    ButtonGroup m = new ButtonGroup();
    m.add(sil);
    m.add(gol);
    m.add(bro);

    b1.setBounds(200,600,100,100);
    b2.setBounds(350,600,100,100);
    b3.setBounds(500,600,100,100);
    b4.setBounds(350,750,100,100);
    f1.add(name);
    f1.add(phno);
    f1.add(nt);
    f1.add(pt);
    f1.add(gender);
    f1.add(male);
    f1.add(female);
    f1.add(membr);
    f1.add(bro);
    f1.add(sil);
    f1.add(gol);
    f1.add(b1);
    f1.add(b2);
    f1.add(b3);
    f1.add(b4);
    f1.getContentPane().setBackground(new Color(161,255,255));

    b1.setBackground(new Color(50,205,50));
    b1.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b1.setBorder(null);
    b1.setFocusPainted(false);

    b2.setBackground(new Color(255,61,65));
    b2.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b2.setBorder(null);
    b2.setFocusPainted(false);

    b3.setBackground(new Color(0,255,255));
    b3.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b3.setBorder(null);
    b3.setFocusPainted(false);

    b4.setBackground(new Color(0,255,255));
    b4.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b4.setBorder(null);
    b4.setFocusPainted(false);

    tab.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e){
            int i = tab.getSelectedRow();
            nt.setText(dtm.getValueAt(i, 1).toString());
            pt.setText(dtm.getValueAt(i, 2).toString());
        }
    });

    b3.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String nm = nt.getText().toString();
            String ph = pt.getText().toString();
            if(DBpractice.checkCust(nm, ph) == 1)
                notice.warn(2);
            else
                notice.warn(-2);
        }
    });

    b1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String name = nt.getText().toString();
            String ph = pt.getText().toString();
            String m = "";
            String g = (male.isSelected() == true)?"M":"F";
            if(sil.isSelected() == true)
                m = "SIL";
            else if(gol.isSelected() == true)
                {m = "GOL";
                gol.setSelected(false);}
            else if(bro.isSelected() == true)
                m = "BRO";
            DBpractice.addData(1,name,ph,g,m);
            Object o[] = new Object[5];
            o[0] = Global.cID;
            o[1] = name;
            o[2] = ph;
            o[3] = g;
            o[4] = m;
            dtm.addRow(o);
            nt.setText("");
            pt.setText("");
        }
    });

    b2.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String name = nt.getText().toString();
            String ph = pt.getText().toString();
            DBpractice.delRecord(1,name,ph);
            if(tab.getSelectedRow() != -1)
            dtm.removeRow(tab.getSelectedRow());
        }
    });

    b4.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
                f1.dispose();
                awtP.EmpInterface();
        }
    });

    f1.setSize(1920,1080);
    
    f1.setLayout(null);
    f1.setVisible(true);
    }
}
class inventory{
    int ano,qty;
    String name;
    float price;
    inventory(int ano,String name,float price,int qty){
        this.ano = ano;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }
    static Object[] getObj(inventory c1){
        Object a[] = new Object[4];
        a[0] = c1.ano;
        a[1] = c1.name;
        a[2] = c1.price;
        a[3] = c1.qty;
        return a; 
    }
    static ArrayList<inventory> getData(){
        ArrayList<inventory> l1 = new ArrayList<inventory>();
        String query = String.format("SELECT * FROM InvTable");
        try{
            PreparedStatement p = (Global.con).prepareStatement(query);
            ResultSet s1 = p.executeQuery();
            while(s1.next()){
                l1.add(new inventory(s1.getInt(1),s1.getString(2),s1.getFloat(3),s1.getInt(4)));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("EXCEPTION592");
        }
        return l1;
    }
    static void interf(){
    JFrame f1 = new JFrame();
    JTable tab = new JTable();
    String col[] = {"Article Number","Name","Price","Quantity"};
    DefaultTableModel dtm = new DefaultTableModel(col,0);
    ArrayList<inventory> l1 = getData();
    for(inventory e:l1){
       dtm.addRow(getObj(e));
    }
    tab.setModel(dtm);
    
    JScrollPane scrollPane = new JScrollPane(tab);
    
    tab.getTableHeader().setFont(new Font("Comic Sans",Font.PLAIN,20));
    tab.setRowHeight(30);
    scrollPane.setFont(new Font("Comic Sans",Font.PLAIN,20));
    tab.setFont(new Font("Comic Sans",Font.PLAIN,20));
    
    scrollPane.setBounds(960,100,800,800);
    
    f1.add(tab.getTableHeader());
    f1.add(scrollPane);

    JButton b1 = new JButton("ADD");
    JButton b2 = new JButton("DELETE");
    JButton b3 = new JButton("SEARCH");
    JButton b4 = new JButton("BACK");
    
    JLabel name = new JLabel("Name");
    JLabel Ano = new JLabel("Article Number");
    JLabel Qty = new JLabel("Quantity");
    JLabel pr = new JLabel("Price");
    
    JTextField nt = new JTextField();
    JTextField pt = new JTextField();
    JTextField at = new JTextField();
    JTextField qt = new JTextField();

    name.setFont(new Font("Comic Sans",Font.BOLD,20));
    Ano.setFont(new Font("Comic Sans",Font.BOLD,20));
    Qty.setFont(new Font("Comic Sans",Font.BOLD,20));
    pr.setFont(new Font("Comic Sans",Font.BOLD,20));
    nt.setFont(new Font("Consolas",Font.PLAIN,20));
    pt.setFont(new Font("Consolas",Font.PLAIN,20));
    at.setFont(new Font("Consolas",Font.PLAIN,20));
    qt.setFont(new Font("Consolas",Font.PLAIN,20));
    
    Ano.setBounds(200,350,150,30);
    name.setBounds(200,400,300,30);
    pr.setBounds(200,450,150,30);
    Qty.setBounds(200,500,150,30);

    at.setBounds(400,350,200,30);
    nt.setBounds(400,400,200,30);
    pt.setBounds(400,450,200,30);
    qt.setBounds(400,500,200,30);
    
    b1.setBounds(200,600,100,100);
    b2.setBounds(350,600,100,100);
    b3.setBounds(500,600,100,100);
    b4.setBounds(350,750,100,100);
    f1.add(name);
    f1.add(Ano);
    f1.add(nt);
    f1.add(pt);
    f1.add(at);
    f1.add(qt);
    f1.add(Qty);
    f1.add(pr);
    f1.add(b1);
    f1.add(b2);
    f1.add(b3);
    f1.add(b4);

    b1.setBackground(new Color(50,205,50));
    b1.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b1.setBorder(null);
    b1.setFocusPainted(false);

    b2.setBackground(new Color(255,61,65));
    b2.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b2.setBorder(null);
    b2.setFocusPainted(false);

    b3.setBackground(new Color(0,255,255));
    b3.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b3.setBorder(null);
    b3.setFocusPainted(false);

    b4.setBackground(new Color(0,255,255));
    b4.setFont(new Font("Fertigo Pro",Font.ITALIC,20));
    b4.setBorder(null);
    b4.setFocusPainted(false);

    tab.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e){
            int i = tab.getSelectedRow();
            at.setText(dtm.getValueAt(i, 0).toString());
            nt.setText(dtm.getValueAt(i, 1).toString());
            pt.setText(dtm.getValueAt(i, 2).toString());
            qt.setText(dtm.getValueAt(i, 3).toString());
        }
    });

    b3.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String an = at.getText().toString();
            if(DBpractice.checkArt(Integer.parseInt(an)) == 1)
                notice.warn(6);
            else
                notice.warn(-6);
        }
    });

    b1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String ano1 = at.getText().toString();
            String name = nt.getText().toString();
            String price = pt.getText().toString();
            String q = qt.getText().toString();
            DBpractice.addData(2,ano1,name,price,q);
            Object o[] = new Object[4];
            o[0] = ano1;
            o[1] = name;
            o[2] = price;
            o[3] = q;
            dtm.addRow(o);
            nt.setText("");
            pt.setText("");
            at.setText("");
            qt.setText("");
        }
    });

    b2.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String a = at.getText().toString();
            DBpractice.delRecord(2,a);
            if(tab.getSelectedRow() != -1)
            dtm.removeRow(tab.getSelectedRow());
            nt.setText("");
            pt.setText("");
            at.setText("");
            qt.setText("");
        }
    });

    b4.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
                f1.dispose();
                awtP.EmpInterface();
        }
    });
    f1.getContentPane().setBackground(new Color(161,255,169));
    f1.setSize(1920,1080);
    
    f1.setLayout(null);
    f1.setVisible(true);
    }
}