/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project2;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author molood
 */
public class MainForm extends javax.swing.JFrame {
   
    private Map<String, Integer> que_train_run_word;
    private Map<String, Integer> train_running_word;    
    private String[] stop_words;
    private int que_train_fv[][][]; // train feature-vectore
    private int que_test_fv[][]; // test feature-vectore
    private String answer[];
    private int num_que[]; // number of questions in each class
    private int num_que_test; // length of set of test
    private double mio[][];  // mean
    private double var[][]; // variance
    private int queClass=18; // number of classes
    private String Train_q[][]; // train data
    private String Test_q[]; // test data
    private String Test_acc[];
    private int test_class[];
     private int Test_num[]; // the true class of each test
   
    
    //-------preProcessing
    public String unification(String str)
    {
        str=str.replace(". ", ".");
        str=str.replace("..", "");
        str=str.replace(":", "");
        str=str.replace("؟", "");
        str=str.replace("؟", "");
        str=str.replace("?", "");
        str=str.replace("،", "");
        str=str.replace("؛", "");
        str=str.replace(" .", ".");
        str=str.replace(") ", " ");
         str=str.replace(")", " ");
        str=str.replace(" )", " ");
        str=str.replace("( ", " ");
        str=str.replace("(", " ");
        str=str.replace(" (", " ");
        str=str.replace("  ", " ");
        str=str.replace("   ", " ");
        str=str.replace("\n", "");
        str=str.replace("ك", "ک");
        str=str.replace("ي", "ی");
        str=str.replace("ئ", "ی");
        str=str.replace("ؤ", "و");
        str=str.replace("ء", "");
        str=str.replace("\n", "");
        str=str.replace("-", "");
        str=str.replace("- ", "");
        str=str.replace(" -", "");
        str=str.replace("[", "");
        str=str.replace("]", "");
        str=str.replace("{", "");
        str=str.replace("}", "");
        str=str.replace("  ", "");
        str=str.replace("  ", "");
        str=str.replace("أ", "ا");
        str=str.replace("ــ", "");
        return str;
    }
    //---------------------initial variables----------------------------------------
    public void init_var(){
       num_que=new int[queClass];
       answer=new String[queClass];
       Train_q=new String[queClass][];
    }
    //---------------Browse training data--------
    public  void Browse_train() {
       
        this.btn_browse_train.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Browse the folder to process");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    
                    txt_train.setText(chooser.getSelectedFile().toString());
                    if(rbn_bayes.isSelected()){

                        try{
                            String file_name=chooser.getSelectedFile().toString();
                            File file = new File(file_name);  
                            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
                            DocumentBuilder db=dbf.newDocumentBuilder();
                            Document document=db.parse(file_name);
                            document.getDocumentElement().normalize();
                            System.out.println("[Root : "+document.getDocumentElement().getNodeName()+" ]");
                            NodeList list[]=new NodeList[queClass];
                            init_var();
                            for(int i=0;i<queClass;i++){
                                list[i]=document.getElementsByTagName("Query"+(i+1));
                                
                                num_que[i]=list[i].getLength();
                                
                                Train_q[i] = new String[num_que[i]];                                
                                for(int j=0;j<list[i].getLength();j++){
                                    Node n=list[i].item(j);
                                    if(n.getNodeType()==Node.ELEMENT_NODE){
                                        Element element=(Element)n;
                                        String q = unification(element.getElementsByTagName("Question").item(0).getTextContent());
                                        String acc = unification(element.getElementsByTagName("acceptable-answer").item(0).getTextContent());
                                        insert_train_array(i,j,q,acc);
                                    }
                                }
                            }
                            set_question_file();
                            get_stop_words();
                            get_que_train_run_word();
                            
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        
                    }
                                                          
                } else {
                JOptionPane.showMessageDialog(null, "please enter your regulation");
                }
            }
        });
    }
    //------------------Browse test data------------------------------------
     public void Browse_test(){
        this.btn_browse_test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Browse the folder to process");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    
                    txt_test.setText(chooser.getSelectedFile().toString());
                    if(rbn_bayes.isSelected()){

                        try{
                            String file_name=chooser.getSelectedFile().toString();
                            File file = new File(file_name);  
                            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
                            DocumentBuilder db=dbf.newDocumentBuilder();
                            Document document=db.parse(file_name);
                            document.getDocumentElement().normalize();
                            System.out.println("[Root test : "+document.getDocumentElement().getNodeName()+" ]");
                            
                            NodeList list[]=new NodeList[queClass];
                            num_que_test=0;
                            Test_num=new int[queClass];
                            for(int i=0;i<queClass;i++){
                                list[i]=document.getElementsByTagName("Query"+(i+1));
                                
                                Test_num[i]=list[i].getLength();
                                num_que_test=num_que_test+Test_num[i];
                            }
                            Test_q=new String[num_que_test];
                            int num=0;
                            for(int c=0;c<queClass;c++){
                                for(int j=0;j<list[c].getLength();j++){
                                    Node n=list[c].item(j);
                                    if(n.getNodeType()==Node.ELEMENT_NODE){
                                        Element element=(Element)n;
                                        if(num<num_que_test){
                                            Test_q[num] = element.getElementsByTagName("Question").item(0).getTextContent();
                                            num++;
                                        }
                                    }
                                }
                            }
                                                      
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        
                    }
                                                          
                } else {
                JOptionPane.showMessageDialog(null, " no test  Selection");
                }
            }
        });
        
    }
//------------------------------------train------------------------------
     public  void Train_Action() {
  
        this.btn_train.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //---------disable all buttons---------------------
                btn_train.setEnabled(false);
                btn_browse_train.setEnabled(false);
                btn_browse_test.setEnabled(false);
                btn_ok.setEnabled(false);
                btn_restore_aeinname.setEnabled(false);
                btn_restore_porsesh.setEnabled(false);
                
                txt_test.setText(null);
                txt_train.setText(null);   
                if(rbn_bayes.isSelected()){
                    get_train_fv();
                    get_test_fv();
                    bayse();
                }
                
                //-----------------finish train---------------------
                 JPanel panel = new JPanel();

                 JOptionPane.showMessageDialog(panel, "Train is done!", "Warning",
                 JOptionPane.WARNING_MESSAGE);
                    
                 btn_train.setEnabled(true);
                 btn_browse_train.setEnabled(true);
                 btn_browse_test.setEnabled(true);
                 btn_ok.setEnabled(true);
                 btn_restore_aeinname.setEnabled(true);
                 btn_restore_porsesh.setEnabled(true);
            }
        });
    }
     public void show_mio(){
        for(int i=0;i<queClass;i++){
            for( int j=0;j<que_train_run_word.size();j++){
                System.out.println(mio[i][j]);
            }
            System.out.println("*");
        }
    }
     public void show_var(){
         System.out.println("===============");
        for(int i=0;i<queClass;i++){
            for( int j=0;j<que_train_run_word.size();j++){
                System.out.println(var[i][j]);
            }
            System.out.println("*");
        }
    }
	//--------------------calculation of running words------------------------
     public void show_train_run_word(){
          Set<Map.Entry<String, Integer>> entry1 = que_train_run_word.entrySet();
             for(Map.Entry<String, Integer> entry : entry1){
                 System.out.println(entry.getKey()+" : "+entry.getValue());
             }        
     }
	 //--------------------calculation of train feature vector-----------------
         for(int i=0;i<queClass;i++){
             for(int j=0;j<num_que[i];j++){
                 for(int w=0;w<que_train_run_word.size();w++)
                      System.out.println(que_train_fv[i][j][w]);
                System.out.println("*"); 
             }
         }
     }
	  //--------------------calculation of test feature vector-----------------
     public void show_que_test_fv(){
         for(int i=0;i<num_que_test;i++){
             for(int j=0;j<que_train_run_word.size();j++){                
                      System.out.println(que_test_fv[i][j]);
             }
                System.out.println("*"); 
             
         }
     }
	  //--------------------lunch answers-----------------
     public void show_ans(){
        for(int i=0;i<answer.length;i++){
            System.out.println(answer[i]);
        }
    }
    //------------navie bayze calculation----------------
    public void zero_que_train_fv(){
       que_train_fv=new int[queClass][][];
        for(int i=0;i<queClass;i++){
            que_train_fv[i]=new int[num_que[i]][que_train_run_word.size()];
        }
        for(int i=0;i<queClass;i++){
            for(int j=0;j<num_que[i];j++){
                for(int k=0;k<queClass;k++){
                    que_train_fv[i][j][k]=0;
                }
            }
        } 
    }
    public void zero_que_test_fv(){
       que_test_fv=new int[num_que_test][que_train_run_word.size()];       
        for(int i=0;i<num_que_test;i++){
            for(int j=0;j<que_train_run_word.size();j++){
                    que_test_fv[i][j]=0;                
            }
        } 
    }
    public void zero_mio_var(){
        mio=new double[queClass][];
        for(int i=0;i<queClass;i++){
            mio[i]=new double[que_train_run_word.size()];
        }
        var=new double[queClass][];
        for(int i=0;i<queClass;i++){
            var[i]=new double[que_train_run_word.size()];
        }
        for(int i=0;i<queClass;i++){
            for(int j=0;j<que_train_run_word.size();j++){
                mio[i][j]=0;
                var[i][j]=0;
            }
        }
    }
    public void get_train_fv(){
        zero_que_train_fv();
        Set<Map.Entry<String, Integer>> entrySet = que_train_run_word.entrySet();
        String []words;
         for(int i=0;i<queClass;i++){
            for(int j=0;j<num_que[i];j++){
                words=Train_q[i][j].split(" ");
                int run_count=0;
                for(Map.Entry<String, Integer> entry : entrySet){
                    for(int c=0;c<words.length;c++){
                        if(entry.getKey().equals(words[c])){
                            que_train_fv[i][j][run_count]=que_train_fv[i][j][run_count]+1;
                        }
                    }
                    run_count++;
                }
            }
         }
    }
    public void get_test_fv(){
        zero_que_test_fv();
        Set<Map.Entry<String, Integer>> entrySet = que_train_run_word.entrySet();
        String []words;
         for(int i=0;i<num_que_test;i++){
                words=Test_q[i].split(" ");
                int run_count=0;
                for(Map.Entry<String, Integer> entry : entrySet){
                    for(int c=0;c<words.length;c++){
                        if(entry.getKey().equals(words[c])){
                            que_test_fv[i][run_count]=que_test_fv[i][run_count]+1;
                        }
                    }
                    run_count++;
                }
            }
         }
    
    public void bayse(){
        
         //show_que_train_fv();
         //---mio
         zero_mio_var();
         for(int i=0;i<queClass;i++){
             for(int k=0;k<que_train_run_word.size();k++){                
                 for(int j=0;j<num_que[i];j++){
                     mio[i][k]=mio[i][k]+que_train_fv[i][j][k];
                 }
                 mio[i][k]=mio[i][k]/num_que[i];
             }             
         }
        // show_mio();
         //---var
        for(int i=0;i<queClass;i++){
             for(int k=0;k<que_train_run_word.size();k++){
                 for(int j=0;j<num_que[i];j++){
                     double cal=que_train_fv[i][j][k]-mio[i][k];
                     cal=cal*cal;
                     var[i][k]=var[i][k]+cal;
                 }
                  var[i][k]=var[i][k]/num_que[i];
             }           
         }
        //show_var();
        //----------classification-----------
        test_class=new int[num_que_test];
        double h[][];
        h=new double[queClass][queClass];
        for(int i=0;i<queClass;i++){
            for(int j=0;j<queClass;j++){
                h[i][j]=0;
            }
        }
        for(int q=0;q<num_que_test;q++){
            int i=0,j=1;
            while(i!=queClass&&j!=queClass){            
                    double covi=1,covj=1;
                    for(int w=0;w<que_train_run_word.size();w++){
                        if(var[i][w]!=0)
                        {
                            double cal1=que_test_fv[q][w]-mio[i][w];
                            cal1=cal1*cal1/var[i][w];
                            h[i][j]=h[i][j]+cal1/2;
                            covi=covi*var[i][w];
                        }
                        if(var[j][w]!=0){
                            double cal2=que_test_fv[q][w]-mio[j][w];
                            cal2=cal2*cal2/var[j][w];
                            h[i][j]=h[i][j]-cal2/2;
                            covj=covj*var[j][w];
                        }
                    }
                    h[i][j]=Math.exp(h[i][j])*(covi/covj);
                    if(h[i][j]<(num_que[j]/num_que[i])){
                        test_class[q]=i;
                        j++;
                    }
                    else{
                        test_class[q]=j;
                        if(i+1==j){
                            i++;
                            j=i+1;
                        }else   i++;
                    }                
            }                       
       
            System.out.println(test_class[q]+1);
            
      }
        // test
            int count=0,jj=0;
            for(int c=0;c<queClass;c++){
                for(int ii=0;ii<Test_num[c];ii++){
                    if(jj<num_que_test){
                        if(test_class[jj]==c)
                            count++;
                        jj++;
                    }
                }
            }
            double percision=(count*100)/num_que_test;
            txt_percision.setText(String.valueOf(percision));
    }
    
     //-------------------GET STOP WORDS--------------------------
     public void get_stop_words(){
                
        try {   
            String stop_file="E:\\fogh\\Term1\\shenasaee olgoo\\project\\project2\\"+
                    "stop_words.txt";
         
            File file = new File(stop_file);
            BufferedReader bufferedReader =new BufferedReader(new FileReader(file));
	
            String inputLine = null;
            Map<String, Integer> stopwords = new HashMap<>();
            
            String[] words;       
            while ((inputLine = bufferedReader.readLine()) != null) {                
                    words = inputLine.split("[ \n\t\r.,;:!?(){}]");               
                    for (int counter = 0; counter < words.length; counter++) {
                        stopwords.put(words[counter],0);
                        }
                    }
            
            Set<Map.Entry<String, Integer>> entrySet = stopwords.entrySet();
            stop_words=new String[stopwords.size()];
            
            int swcounter=0;
            for(Map.Entry<String, Integer> entry : entrySet){
                stop_words[swcounter++]=entry.getKey();
            }
            
            bufferedReader.close();
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "there isn't stop words file!");
        }
     }
     
    public void set_question_file(){
        try{
            String question="E:\\fogh\\Term1\\shenasaee olgoo\\project\\project2\\questions.txt";
            BufferedWriter que_write = null;
            que_write = new BufferedWriter(new FileWriter(question));
            for(int i=0;i<queClass;i++){
                for(int j=0;j<num_que[i];j++){
                    que_write.write(Train_q[i][j]);
                    que_write.newLine();
                }
            }
            que_write.flush();
            que_write.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "can not set question file");
        }
    }
    
   public void insert_train_array(int i,int j,String q1,String acc1){
        Train_q[i][j]=q1;   
        answer[i]=acc1;        
    }
 
   public void get_que_train_run_word(){
       try{
            String question="E:\\fogh\\Term1\\shenasaee olgoo\\project\\project2\\questions.txt";
            que_train_run_word=new HashMap<>();
            File train_file=new File(question);
            BufferedReader train_reader=new BufferedReader(new FileReader(train_file));
            String inputLine = null;
            Map<String, Integer> crunchifyMap = new HashMap<>();
            
            String[] words;
            //----find words with frequency------------------
            while ((inputLine = train_reader.readLine()) != null) {                
                    words = inputLine.split("[ \n\t\r.,;:!?(){}]");               
                    for (int counter = 0; counter < words.length; counter++) {
                        String key = words[counter].toLowerCase(); // remove .toLowerCase for Case Sensitive result.
                        if (key.length() > 0) {
                            if (crunchifyMap.get(key) == null) {
                                crunchifyMap.put(key, 1);
                            } else {
                                    int value = crunchifyMap.get(key).intValue();
                                    value++;
                                    crunchifyMap.put(key, value);
                                    }
                        }
                    }
            }
            
            Set<Map.Entry<String, Integer>> entrySet = crunchifyMap.entrySet();
            //---------delete stop words from------------------------
            for(Map.Entry<String, Integer> entry : entrySet){
                boolean flag=true;
                
                for(int counter=0; counter<stop_words.length;counter++){
                    if(entry.getKey().equals(stop_words[counter])){
                   // if(entry.getKey()==stop_words[counter]){
                        flag=false;
                        break;
                    }
                }
                
                if(flag){
                    que_train_run_word.put(entry.getKey(), entry.getValue());
                }
            }
            //---------------------------
           /* Set<Map.Entry<String, Integer>> entry1 = que_train_run_word.entrySet();
             for(Map.Entry<String, Integer> entry : entry1){
                 System.out.println(entry.getKey()+" : "+entry.getValue());
             }*/
       }catch(IOException e){
            JOptionPane.showMessageDialog(null, "there is no question file");
        }
       
   }
     
     //-------------------------------
     
    public MainForm() {
        initComponents();
        Browse_train();
        Browse_test();
        Train_Action();     
    }
    
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngrp_state = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_test = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_train = new javax.swing.JTextField();
        btn_browse_train = new javax.swing.JButton();
        btn_browse_test = new javax.swing.JButton();
        btn_restore_aeinname = new javax.swing.JButton();
        btn_restore_porsesh = new javax.swing.JButton();
        btn_train = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txt_soal = new javax.swing.JTextField();
        btn_ok = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_answer = new javax.swing.JTextPane();
        btn_exit = new javax.swing.JButton();
        txt_percision = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rbn_bayes = new javax.swing.JRadioButton();
        rbn_state1 = new javax.swing.JRadioButton();
        rbn_nearest_neighbor = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("قسمت آموزش");

        jLabel2.setText("فایل آموزش را وارد کنید:");

        txt_test.setName("txt_test"); // NOI18N

        jLabel3.setText("فایل تست را وارد کنید:");

        txt_train.setName("txt_train"); // NOI18N

        btn_browse_train.setLabel("Browse...");
        btn_browse_train.setName("btn_browse_train"); // NOI18N

        btn_browse_test.setLabel("Browse...");
        btn_browse_test.setName("btn_browse_test"); // NOI18N

        btn_restore_aeinname.setLabel("Restore");
        btn_restore_aeinname.setName("btn_restore_aeinname"); // NOI18N

        btn_restore_porsesh.setLabel("Restore");
        btn_restore_porsesh.setName("btn_restore_porsesh"); // NOI18N

        btn_train.setLabel("Train");
        btn_train.setName("btn_train"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(141, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn_restore_aeinname)
                                .addGap(18, 18, 18)
                                .addComponent(btn_browse_train))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn_restore_porsesh)
                                .addGap(18, 18, 18)
                                .addComponent(btn_browse_test)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_test, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                            .addComponent(txt_train))))
                .addGap(26, 26, 26))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_train, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_train, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_browse_train)
                    .addComponent(btn_restore_aeinname))
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_test, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_browse_test)
                    .addComponent(btn_restore_porsesh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(btn_train)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("قسمت تست");

        txt_soal.setName("txt_soal"); // NOI18N

        btn_ok.setLabel("OK");
        btn_ok.setName("btn_ok"); // NOI18N

        jLabel5.setText("سوال خود را وارد کنید:");

        jLabel6.setText("پاسخ:");

        jScrollPane1.setViewportView(txt_answer);

        btn_exit.setText("Exit");

        jLabel7.setText("درصد تشخیص درست :");

        btngrp_state.add(rbn_bayes);
        rbn_bayes.setText("بیز");

        btngrp_state.add(rbn_state1);
        rbn_state1.setText("حالت اول");

        btngrp_state.add(rbn_nearest_neighbor);
        rbn_nearest_neighbor.setText("نزدیک ترین همسایه");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 543, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(rbn_state1)
                                            .addComponent(rbn_nearest_neighbor)
                                            .addComponent(rbn_bayes))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_ok))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txt_percision, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_soal, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1))))
                        .addGap(28, 28, 28))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_exit)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_soal, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_ok))
                        .addGap(39, 39, 39)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(rbn_state1)
                        .addGap(7, 7, 7)
                        .addComponent(rbn_nearest_neighbor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbn_bayes)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_percision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_exit))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
       
        try {
            
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_browse_test;
    private javax.swing.JButton btn_browse_train;
    private javax.swing.JButton btn_exit;
    private javax.swing.JButton btn_ok;
    private javax.swing.JButton btn_restore_aeinname;
    private javax.swing.JButton btn_restore_porsesh;
    private javax.swing.JButton btn_train;
    private javax.swing.ButtonGroup btngrp_state;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbn_bayes;
    private javax.swing.JRadioButton rbn_nearest_neighbor;
    private javax.swing.JRadioButton rbn_state1;
    private javax.swing.JTextPane txt_answer;
    private javax.swing.JTextField txt_percision;
    private javax.swing.JTextField txt_soal;
    private javax.swing.JTextField txt_test;
    private javax.swing.JTextField txt_train;
    // End of variables declaration//GEN-END:variables
}
