/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project;

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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author molood
 */
public class MainForm extends javax.swing.JFrame {
    private String train;
    private String test;
    private String[] stop_words;
    private Map<String, Integer> train_featureVector;
    private int sentense_featureVector[][];
    
    
    //--------------ADD LINE TO END OF FILE----------
    public void addLine(String file_address){
        FileWriter fStream;
       try {
            fStream = new FileWriter(file_address, true);
            fStream.append(System.getProperty("line.separator"));
            fStream.flush();
            fStream.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "can not add new line in file!");
        }
    }
    //------------DELETE FILE----------------------
    public void delete_file(String file_address){
        try{
    		
    		File file = new File(file_address);
        	
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			//System.out.println("Delete operation is failed.");
                    JOptionPane.showMessageDialog(null, "can not delete the file!");
    		}
    	   
    	}catch(Exception e){
    		JOptionPane.showMessageDialog(null, "there is a problem in delete file");
    		e.printStackTrace();
    		
    	}
    }
            
    //-----------------concat 2 file---------------
    //srFile ra b entehaye dtFile ezafe mikonad-->dtFilesrFile
    public void concat(String srFile, String dtFile){
        try{
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);
             
            //For Append the file.
            OutputStream out = new FileOutputStream(f2,true);
 
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        }
        catch(FileNotFoundException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage() + " in the specified directory.");
            //System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        }
        catch(IOException e){
            //System.out.println(e.getMessage());  
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    //-------------------UNIFICATE STRING-------------------
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
        str=str.replace(" )", " ");
        str=str.replace("( ", " ");
        str=str.replace(" (", " ");
        str=str.replace("  ", " ");
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
        return str;
    }
    //---------------------UNIFICATE FILE----------------------------
    public void unification_file(String primary_file_address,String unificated_file_address){
        
        try{
            File primary_file=new File(primary_file_address);
            BufferedReader read_primary_file=new BufferedReader(new FileReader(primary_file));
        
            String inputLine=null;
                   
            BufferedWriter write_unificated_file = null;
            write_unificated_file = new BufferedWriter(new FileWriter(unificated_file_address));
            
            while((inputLine=read_primary_file.readLine())!=null){
                inputLine=unification(inputLine);
                //System.out.println(inputLine);
                write_unificated_file.write(inputLine);
                write_unificated_file.newLine();
            }
            
            read_primary_file.close();
            write_unificated_file.flush();
            write_unificated_file.close();
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "can not unificate the file!");
        }
    
    
    }
    //-------------------Browse aeinname-----------------
     public  void Browse_aeinname() {
       
        this.btn_browse_aeinname.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Browse the folder to process");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    
                    txt_aeinname.setText(chooser.getSelectedFile().toString());
                    addLine(chooser.getSelectedFile().toString());
                    concat(chooser.getSelectedFile().toString(), train);
                    
                    
                    
                } else {
                //System.out.println("No Selection ");
                JOptionPane.showMessageDialog(null, " no Regulation Selected!");
                }
            }
        });
    }
     //-------------------------Restore aein name-----------------------
     public  void Restore_aeinname() {
  
        this.btn_restore_aeinname.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txt_aeinname.setText(null);
                 delete_file(train);
                
            }
        });
    }   
    //-------------------------Browse porsesh name-----------------
     public  void Browse_porsesh() {
       
        this.btn_browse_porsesh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Browse the folder for processing");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {               
                    txt_porsesh.setText(chooser.getSelectedFile().toString());   
                    addLine(chooser.getSelectedFile().toString());
                    concat(chooser.getSelectedFile().toString(), test);
                    
                    
                } else {
                //System.out.println("No Selection ");
                JOptionPane.showMessageDialog(null, "please enter your questioner");
                }
            }
        });
    }
     //--------------------------Restore questioner----------------------
     public  void Restore_porsesh() {
  
        this.btn_restore_porsesh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txt_porsesh.setText(null);
                 delete_file(test);               
            }
        });
    }
       //-------------------GET STOP WORDS--------------------------
     public void get_stop_words(){
        
               
        try {   
            String stop_file="E:\\master\\Term1\\nlp\\projects\\project06\\"+
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
     //--------------GET RUNNING WORDS (INITIAL MAIN FEATURE VECTOR)
     public void get_trainFeatureVector(){
         try{
            train_featureVector=new HashMap<>();
            File train_file=new File(train);
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
                    train_featureVector.put(entry.getKey(), entry.getValue());
                }
            }
            
         }catch(IOException e){
             JOptionPane.showMessageDialog(null, "there is no unificated train for feature vector");
         }
         
     }
     //-------------GET SENTENCE FEATURE VECTOR------------------------
     public void get_sentenseFeatureVector(){
         
           int lines_of_train=find_num_sentense(train);
           sentense_featureVector=new int[lines_of_train+1][train_featureVector.size()];
           int counter_row,counter_column;
                     
           
           for(int i=0;i<lines_of_train+1;i++){
               for(int j=0;j<train_featureVector.size();j++){
                   sentense_featureVector[i][j]=0;
               }
           }
           
         try{
            
            File train_file=new File(train);
            BufferedReader train_reader=new BufferedReader(new FileReader(train_file));
            
            String inputLine = null;
            
            
            String[] words;
            counter_row=0;
            
            while ((inputLine = train_reader.readLine()) != null) {     
                //----find words with frequency------------------
                    words = inputLine.split("[ \n\t\r.,;:!?(){}]"); 
                    Map<String, Integer> sentense = new HashMap<>();
                    for (int counter = 0; counter < words.length; counter++) {
                        String key = words[counter].toLowerCase(); // remove .toLowerCase for Case Sensitive result.
                        if (key.length() > 0) {
                            if (sentense.get(key) == null) {
                                sentense.put(key, 1);
                            } else {
                                    int value = sentense.get(key).intValue();
                                    value++;
                                    sentense.put(key, value);
                                    }
                        }
                    }
                   //------------------------------- 
                    Set<Map.Entry<String, Integer>> sentenseSet = sentense.entrySet();
                    Set<Map.Entry<String, Integer>> trainSet = train_featureVector.entrySet();
            
                    counter_column=0;
            //----------------set sentense feature vector-------------------
                    for (Map.Entry<String, Integer> entryTrain : trainSet) {
                
                        for (Map.Entry<String, Integer> entrySentense : sentenseSet) {	
                            
                            if(entryTrain.getKey().equals(entrySentense.getKey())){
                                sentense_featureVector[counter_row][counter_column]=entrySentense.getValue();
                            }                    
                        }
                
                        counter_column++;
                    }
                    counter_row++;
                }
            //----feature vector sentense ro mizare to yek file
            String matn_file="E:\\master\\Term1\\nlp\\projects\\project06\\fv.txt";
        
            BufferedWriter outputWriter_word = null;
            outputWriter_word = new BufferedWriter(new FileWriter(matn_file));
            
                for(int i=0;i<lines_of_train+1;i++){
                    for(int j=0;j<train_featureVector.size();j++){
                        //System.out.print(sentense_featureVector[i][j]+"\t");  
                        outputWriter_word.write(Integer.toString(sentense_featureVector[i][j])+" ");
                    }
                    //System.out.println("\n");
                    outputWriter_word.newLine();
                    outputWriter_word.newLine();
                    outputWriter_word.newLine();
                    
                }
                //
                train_reader.close();
                 outputWriter_word.flush();  
                outputWriter_word.close();  
            
            
            }catch(IOException e){
             JOptionPane.showMessageDialog(null, "problem for setting sentense feature vector");
         }
     }
     //------------------delete stop words from train----------------------
     public void del_sw_train(){
         try{
            File train_file=new File(train);
            BufferedReader train_reader=new BufferedReader(new FileReader(train_file));
            
            String inputLine = null;
                       
            String train_sw="E:\\master\\Term1\\nlp\\projects\\project06\\"+
                            "train_stop_words.txt";
             BufferedWriter train_stop = null;
             train_stop = new BufferedWriter(new FileWriter(train_sw));
            
            String[] words;
            
            while ((inputLine = train_reader.readLine()) != null) {                
                    words = inputLine.split("[ \n\t\r.,;:!?(){}]");               
                    for (int counter = 0; counter < words.length; counter++) {
                        boolean flag=true;
                        for(int counter1=0; counter1<stop_words.length;counter1++){
                            if(words[counter].equals(stop_words[counter1])){
                                flag=false;
                                break;                       
                            }
                        }
                        if(flag)
                            train_stop.write(words[counter]+" ");
                    }
                    train_stop.write(".");
                    train_stop.newLine();
                    //---add line
                    
            }
            
         }catch(IOException e){
             JOptionPane.showMessageDialog(null, "there is no unificated train for feature vector");
         }
     }
     //------------------------find number of sentenses in train---------
     public int find_num_sentense(String str){
         int lines_of_train = 0;
         try{
             
            BufferedReader reader = new BufferedReader(new FileReader(str));        
            while (reader.readLine() != null) lines_of_train++;
            reader.close();
            
         }catch(IOException e){
             JOptionPane.showMessageDialog(null, "there is no train file for found the number of sentence");
         }
         return lines_of_train;
     }
     //----------------------------Train action-----------------------------
     public  void Train_Action() {
  
        this.btn_train.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //---------disable all buttons---------------------
                btn_train.setEnabled(false);
                btn_browse_aeinname.setEnabled(false);
                btn_browse_porsesh.setEnabled(false);
                btn_ok.setEnabled(false);
                btn_restore_aeinname.setEnabled(false);
                btn_restore_porsesh.setEnabled(false);
                
                txt_porsesh.setText(null);
                txt_aeinname.setText(null);
                //------------------UNIFICATE TRAIN-----------------
                String unificated_train="E:\\master\\Term1\\nlp\\projects\\project06\\main_train_unificated.txt";
                    
                    unification_file(train, unificated_train);
                    
                    train=unificated_train;
                    get_stop_words();
                    del_sw_train();
                    
                    get_trainFeatureVector();
                    get_sentenseFeatureVector();
                    
                    
                    // print running words
                   try{ 
                       String run_adderc="E:\\master\\Term1\\nlp\\projects\\project06\\run_adder_count.txt";
                     BufferedWriter runningWordsc = null;
                    runningWordsc = new BufferedWriter(new FileWriter(run_adderc));
                    
                    String run_adder="E:\\master\\Term1\\nlp\\projects\\project06\\run_adder.txt";
                     BufferedWriter runningWords = null;
                    runningWords = new BufferedWriter(new FileWriter(run_adder));
                    
                    System.out.println("==================================");
                    Set<Map.Entry<String, Integer>> entrySet=train_featureVector.entrySet();
                    int i=1;
                    for(Map.Entry<String, Integer> entry : entrySet){
                        System.out.println(i+" : "+entry.getKey()+" : "+entry.getValue());
                        i++;
                        runningWordsc.write(entry.getKey()+" "+(Integer.toString(entry.getValue())));
                        runningWordsc.newLine();
                        
                        runningWords.write(entry.getKey());
                        runningWords.newLine();
                    }
                    
                    runningWordsc.flush();
                    runningWordsc.close();
                    runningWords.flush();
                    runningWords.close();
                   }catch(IOException e){
                       System.out.println("can not running words");
                   }
                        
                //-------------------UNIFICAT TEAST----------------
               /* String unificated_test="E:\\master\\Term1\\nlp\\projects\\project06\\main_test_unificated.txt";
                    
                unification_file(test, unificated_test);
                    
                test=unificated_test;*/
                
                //-----------------finish train---------------------
                 JPanel panel = new JPanel();

                 JOptionPane.showMessageDialog(panel, "Train is done!", "Warning",
                 JOptionPane.WARNING_MESSAGE);
                    
                 btn_train.setEnabled(true);
                 btn_browse_aeinname.setEnabled(true);
                 btn_browse_porsesh.setEnabled(true);
                 btn_ok.setEnabled(true);
                 btn_restore_aeinname.setEnabled(true);
                 btn_restore_porsesh.setEnabled(true);
            }
        });
    }
     
   //------------------user ask question and clicks ok to see his answer--
     public  void show_answer() {
  
        this.btn_ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JPanel panel = new JPanel();
                
                 
                if(txt_soal.getText().isEmpty()){
					// please ask your question
                    JOptionPane.showMessageDialog(panel, "لطفا سوال خود را بپرسید!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
                }
                else{
					// waiting for processing
                    JOptionPane.showMessageDialog(panel, "اوپراتورها مشغول می باشند!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
                }             
            }
        });
    }
     //-------------------------EXIT-----------------------------------
     public  void exit() {
  
        this.btn_exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String train_m="E:\\master\\Term1\\nlp\\projects\\project06\\main_train.txt";
                String train_uni="E:\\master\\Term1\\nlp\\projects\\project06\\main_train_unificated.txt";
                String train_sw="E:\\master\\Term1\\nlp\\projects\\project06\\train_stop_words.txt";
                delete_file(train_m);
                delete_file(train_uni);
                delete_file(train_sw);
                 System.exit(0);         
            }
        });
    }
   
     
     //-------------------------------
     
    public MainForm() {
        initComponents();
        //txt_soal=null;
                  
        train="E:\\master\\Term1\\nlp\\projects\\project06\\main_train.txt";
        test="E:\\master\\Term1\\nlp\\projects\\project06\\main_test.txt";
        
        Browse_aeinname();
        Browse_porsesh();
        Restore_aeinname();
        Restore_porsesh();
        Train_Action();
         show_answer();
         exit();
         
    }
    
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_porsesh = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_aeinname = new javax.swing.JTextField();
        btn_browse_aeinname = new javax.swing.JButton();
        btn_browse_porsesh = new javax.swing.JButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("قسمت آموزش");

        jLabel2.setText("آیین نامه را وارد کنید:");

        txt_porsesh.setName("txt_porsesh"); // NOI18N

        jLabel3.setText("پرسش نامه را وارد کنید:");

        txt_aeinname.setName("txt_aeinname"); // NOI18N

        btn_browse_aeinname.setLabel("Browse...");
        btn_browse_aeinname.setName("btn_browse_aeinname"); // NOI18N

        btn_browse_porsesh.setLabel("Browse...");
        btn_browse_porsesh.setName("btn_browse_porsesh"); // NOI18N

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
                .addContainerGap(137, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_restore_aeinname)
                        .addGap(18, 18, 18)
                        .addComponent(btn_browse_aeinname)
                        .addGap(18, 18, 18)
                        .addComponent(txt_aeinname, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_restore_porsesh)
                        .addGap(18, 18, 18)
                        .addComponent(btn_browse_porsesh)
                        .addGap(18, 18, 18)
                        .addComponent(txt_porsesh, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(txt_aeinname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_browse_aeinname)
                    .addComponent(btn_restore_aeinname))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_porsesh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_browse_porsesh)
                        .addComponent(btn_restore_porsesh)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(btn_train)
                .addContainerGap())
        );

        jLabel4.setText("قسمت تست");

        txt_soal.setName("txt_soal"); // NOI18N

        btn_ok.setLabel("OK");
        btn_ok.setName("btn_ok"); // NOI18N

        jLabel5.setText("سوال خود را وارد کنید:");

        jLabel6.setText("پاسخ:");

        jScrollPane1.setViewportView(txt_answer);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(199, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_ok)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_soal, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))))
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_soal, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ok))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        btn_exit.setText("Exit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_exit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_exit)))
                .addContainerGap())
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
    private javax.swing.JButton btn_browse_aeinname;
    private javax.swing.JButton btn_browse_porsesh;
    private javax.swing.JButton btn_exit;
    private javax.swing.JButton btn_ok;
    private javax.swing.JButton btn_restore_aeinname;
    private javax.swing.JButton btn_restore_porsesh;
    private javax.swing.JButton btn_train;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txt_aeinname;
    private javax.swing.JTextPane txt_answer;
    private javax.swing.JTextField txt_porsesh;
    private javax.swing.JTextField txt_soal;
    // End of variables declaration//GEN-END:variables
}
