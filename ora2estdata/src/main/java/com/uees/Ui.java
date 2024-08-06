package com.uees;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


public class Ui extends JFrame {

    private com.uees.GraphLA<String> grafo;
    public Ui() {
        super("Oracle Of Bacon");

        /** Instanciamos de nuevo movies */
        LinkedList<Movie> movies = new LinkedList<>();
        grafo = new com.uees.GraphLA<>(false);

        LinkedList<String> uniqueActors = new LinkedList<>();
        // Copiamos los nombres de los actores
        JButton button = new JButton("Encontrar Numero");
        JLabel label3 = new JLabel("Hasta Actor:");


        JComboBox<String> comboBoxSrc = new JComboBox<>();
        JComboBox<String> comboBoxDest = new JComboBox<>();

        JLabel label2 = new JLabel("Desde Actor:");
        JLabel titleLabel = new JLabel("Movie Oracle", SwingConstants.CENTER);
        
        //Creamos el panel
        JPanel center = new JPanel(new GridBagLayout());

        // Se enruta y leemos Dataset
        String csvFile = "OracleOfBacon-EstDatos2-main/ora2estdata/src/main/resources/dataset/actorfilms.csv";
        String csvSplitBy = ",";
        String line = "";
        



        //Empezamos la lectrua linea a linea
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) 
        {
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(csvSplitBy);
                if (fields.length < 7) {
                    continue;
                }

                String actor = fields[0];
                String actorID = fields[1];
                String film = fields[2];
                String filmID = fields[6];

                Movie movie = new Movie(actor, actorID, film, filmID); //El objeto tipo Movie
                movies.add(movie);
            }

            //Termina la lectura, al hacerlo con un While es medio demorado
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Agregamos todos a la combobox per con los actores extraidos
        for (Movie movie : movies) {
            if (!uniqueActors.contains(movie.getActor())) {
                uniqueActors.add(movie.getActor());
            }
        }
    
        Collections.sort(uniqueActors);

        //Agregamos los actores, no repetidos a la combobox 
        for (String actor : uniqueActors) {
            comboBoxSrc.addItem(actor);
            comboBoxDest.addItem(actor);
        }
    
        System.out.println("Peliculas: " + movies.size());
        System.out.println("Actores: " + uniqueActors.size());
        HashMap<String, HashSet<String>> filmActorsMap = new HashMap<>();

        // Create a map of films to actors
        for (Movie movie : movies) {
            String film = movie.getFilm(); //Extraemos de tipo string la pelicula y los actores
            String actor = movie.getActor(); 


            filmActorsMap.putIfAbsent(film, new HashSet<>());
            filmActorsMap.get(film).add(actor);
        }
        // Los vertices tendran como peso los actores, agregamos uno a uno
        for (String actor : uniqueActors) {
            grafo.addVertex(actor);
        }

        // Los nodos, o edges tendran las peliculas, cada nodo sumara una parada
        for (HashSet<String> actors : filmActorsMap.values()) { //recorremos las peliculas
            for (String actor1 : actors) {                      //Iterar sobre todos los actores en el conjunto actual:
                for (String actor2 : actors) {                  //Emparejar cada actor con todos los demás actores en el mismo conjunto:
                    if (!actor1.equals(actor2)) {               //Evitar la autoconexión
                        grafo.addEdge(actor1, actor2, 1);//Añadimos la arista
                    }
                }
            }
        }
        System.out.println("Grafo creado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        addComponents(center, label2, comboBoxSrc, label3, comboBoxDest, button, titleLabel, center);

        setPreferredSize(new Dimension(400, 300));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void addComponents(JPanel panel, JLabel label2, JComboBox<String> comboBoxSrc, JLabel label3,
            JComboBox<String> comboBoxDest, JButton button, JLabel titleLabel, JPanel center) {

        // Center
        GridBagConstraints gbc = new GridBagConstraints();
        ;
        gbc.insets.set(10, 10, 10, 10);// Padding

        comboBoxDest.setMinimumSize(new Dimension(150, 20));
        comboBoxDest.setPreferredSize(new Dimension(150, 20));

        comboBoxSrc.setMinimumSize(new Dimension(150, 20));
        comboBoxSrc.setPreferredSize(new Dimension(150, 20));

        if (label2 != null && comboBoxSrc != null && label3 != null && comboBoxDest != null && button != null
                && center != null) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2; 
            center.add(titleLabel, gbc);
            gbc.gridwidth = 1;

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            center.add(label2, gbc);
            gbc.gridx = 1;
            center.add(comboBoxSrc, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            center.add(label3, gbc);
            gbc.gridx = 1;
            center.add(comboBoxDest, gbc);

            add(center, BorderLayout.CENTER);
   
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2; 
            center.add(button, gbc);

            button.addActionListener(e -> {
                String src = comboBoxSrc.getSelectedItem().toString();
                String dest = comboBoxDest.getSelectedItem().toString();
                System.out.println("From: " + src + " To: " + dest);
                if(grafo == null) {
                    System.out.println("Error: Null graph");
                    return;
                }
                grafo.dijkstra(src);
                System.out.println("Path from " + src );
                LinkedList<Vertex<String>> tmp = grafo.pathFindertoVertex(dest);
                PopUp pop = new PopUp(tmp.size());
                tmp.forEach(a -> {
                    pop.addLabel(a.getData());
                    pop.addLabel("↓");
                });
                pop.addLabel("_____");
                pop.setVisible(true);



                return;
 
            });

        } else {
            System.out.println("Error: Null component");
        }

    }

    public static void initiateGui() {
        new Ui();
    }

    // Test Main
    public static void main(String[] args) {
        initiateGui();
    }

}
