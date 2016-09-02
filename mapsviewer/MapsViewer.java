/*
 * Developed by Lucas Brandt (aka "Citric" or "CitricLucas")
 * feel free to improve upon, criticize, use, worship, mock, modify, etc. this code
 * just a quick project because it's something I wanted to use myself
 */
package mapsviewer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;


public class MapsViewer {
    
    private static class ViewedGUI extends JPanel {
      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         g.drawString( "Hello World!", 20, 30 );
      }
   }
   
   private static class ButtonHandler implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         System.exit(0);
      }
   }

    public static void main(String[] args) {
        String filepath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Team Fortress 2\\tf\\media.\\viewed.res";
        Scanner input;
        
        ArrayList<Map> mapList = new ArrayList<>();
        ArrayList<Prefix> prefixList = new ArrayList<>();
        
        try {
            input = new Scanner(new FileInputStream(filepath));
        } catch(FileNotFoundException e) {
            try {
                input = new Scanner(new FileInputStream("./viewed.res"));
                return;
            } catch(FileNotFoundException e2) {
                String warning = "viewed.res could not be found in the standard Windows location.\nPlease copy the file and paste it in the folder where you are running this application, and run again.";
                System.out.println(warning);
                
                JButton okButton = new JButton("OKAY");
                ButtonHandler listener = new ButtonHandler();
                okButton.addActionListener(listener);

                JTextArea text = new JTextArea(warning);
                JPanel content = new JPanel();
                content.setLayout(new BorderLayout());
                content.add(text, BorderLayout.CENTER);
                content.add(okButton, BorderLayout.SOUTH);

                JFrame window = new JFrame("viewed.res");
                window.setContentPane(content);
                window.pack();
                window.setLocation(100, 100);
                window.setVisible(true);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                return;
            } 
        }
        
        if(input.hasNextLine() == false) {
            String warning = "viewed.res is empty.";
            System.out.println(warning);

            JButton okButton = new JButton("OKAY");
            ButtonHandler listener = new ButtonHandler();
            okButton.addActionListener(listener);

            JTextArea text = new JTextArea(warning);
            JPanel content = new JPanel();
            content.setLayout(new BorderLayout());
            content.add(text, BorderLayout.CENTER);
            content.add(okButton, BorderLayout.SOUTH);

            JFrame window = new JFrame("viewed.res");
            window.setContentPane(content);
            window.pack();
            window.setLocation(100, 100);
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            return;
        }
        
        //clear first two lines
        input.nextLine();
        input.nextLine();
        
        while(input.hasNextLine() ) {
            //	"koth_viaduct"
            String nameLine = input.nextLine();
            if(nameLine.contains("\"") == false) {
                break;
            }
            
            //clear open bracket line
            input.nextLine();
            
            //		"viewed"		"161"
            String numLine = input.nextLine();
            
            //clear close bracket line
            input.nextLine();
            
            Map map = new Map(nameLine, numLine);
            
            mapList.add(map);
            
            boolean exists = false;
            for(Prefix element : prefixList) {
                if(element.getStr().equals(map.getPrefix())) {
                    exists = true;
                    element.addOne();
                }
            }
            if(exists == false) {
                prefixList.add(new Prefix(map.getPrefix(), 1));
            }
            
        }
        
        Collections.sort(mapList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                return (o2.getLoaded() - o1.getLoaded());
            }
        });
        
        Collections.sort(prefixList, new Comparator<Prefix>() {
            @Override
            public int compare(Prefix o1, Prefix o2) {
                return (o2.getNum() - o1.getNum());
            }
        });
        
        StringBuilder initialText = new StringBuilder();
        for (Map element : mapList)
            initialText.append("      " + element.toString() + "\n");
        
        final DefaultComboBoxModel selectorModel = new DefaultComboBoxModel();
        selectorModel.addElement("All maps");
        for (Prefix element : prefixList) {
            selectorModel.addElement(element.toString());
        }
        final JComboBox select = new JComboBox(selectorModel);
        select.setSelectedIndex(0);
        
        int numMaps = mapList.size();
        int totalLoaded = 0;
        for (Map element : mapList)
            totalLoaded += element.getLoaded();

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        final JTextArea stats = new JTextArea("  You have loaded " + numMaps + " maps a total of " + totalLoaded + " times.\n");
        final JTextArea text = new JTextArea(initialText.toString());
        DefaultCaret caret = (DefaultCaret) text.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        final JScrollPane sp = new JScrollPane(text);
        
        ActionListener cbActionListener = new ActionListener() {//add actionlistner to listen for change
            @Override
            public void actionPerformed(ActionEvent e) {

                String s = (String) select.getSelectedItem();//get the selected item
                s = s.substring(0, s.indexOf(" "));

                if(s.equals("All")) {
                    int totalLoaded = 0;
                    int numMaps = mapList.size();
                    for (Map element : mapList)
                        totalLoaded += element.getLoaded();
                    
                    stats.setText("  You have loaded " + numMaps + " maps a total of " + totalLoaded + " times.\n");
                    StringBuilder allMaps = new StringBuilder();
                    for(Map element : mapList) {
                        allMaps.append("      " + element.toString() + "\n");
                    }
                    text.setText(allMaps.toString());
                    sp.getVerticalScrollBar().setValue(0);
                    
                } else {
                    int totalLoaded = 0;
                    int numMaps = 0;
                    
                    for (Map element : mapList) {
                        if(element.getPrefix().equals(s)) {
                            totalLoaded += element.getLoaded();
                            numMaps++;
                        }
                    }
                    
                    stats.setText("  You have loaded " + numMaps + " " + s + " maps a total of " + totalLoaded + " times.\n");
                    
                    StringBuilder chosenMaps = new StringBuilder();
                    for(Map element : mapList) {
                        if(element.getPrefix().equals(s)) {
                            chosenMaps.append("      " + element.toString() + "\n");
                        }
                    }
                    text.setText(chosenMaps.toString());
                    sp.getVerticalScrollBar().setValue(0);
                }
            }
        };
        select.addActionListener(cbActionListener);
        
        sp.setPreferredSize(new Dimension(700, 500));
        sp.getVerticalScrollBar().setValue(0);
        textPanel.add(stats, BorderLayout.NORTH);
        textPanel.add(sp, BorderLayout.SOUTH);
        
        JButton okButton = new JButton("DONE");
        ButtonHandler listener = new ButtonHandler();
        okButton.addActionListener(listener);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(select, BorderLayout.NORTH);
        content.add(textPanel, BorderLayout.CENTER);
        content.add(okButton, BorderLayout.SOUTH);

        JFrame window = new JFrame("viewed.res");
        window.setContentPane(content);
        window.pack();
        window.setLocation(100,100);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
