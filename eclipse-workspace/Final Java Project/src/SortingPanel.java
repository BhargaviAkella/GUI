
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**The SortPanel class encapsulates the display and controls for the sorting animation and inherits from JPanel.  */
public class SortingPanel extends JPanel
{

 //This is used for assigning the speed of the delay
 private final static int THREAD_SLEEP = 10;
 //private JButton to sort the elements
 private JButton sortButton;
 //private JButton to sort the elements in reverse order
 private JButton sortButtonRev;
//private JButton to stop the sorting
 private  JButton stopButton;
 //JButton to populate the array of elements
 private JButton populateButton;
 //JComboBox used to choose the sorting method to apply
 private JComboBox<String> methodSelector;
 //JComboBox used to select the sorting method 
 private JComboBox<String> populateSelector;
 //JComboBox used to select the sorting order
 private JComboBox<String> SpeedSelector;
 //This is used to display the sorting
 private SortAnimationPanel sortDisplay;
 //private Thread object
 private Thread t;
 //Array containing the element to sort
 private int[] array;
 //boolean value used for sorting 
 boolean ascending;
 
 public SortingPanel() 
 {
  //array of Strings to hold the sorting names 
  String[] elements = {"Insertion sort","Quick sort", "Bubble sort"};
  //array of Strings to hold the sorting order 
  String[] populate_elements = {"Random","Ascending","Descending"};
  //array of Strings to hold the sorting speed 
  String[] Speed = {"FAST","MEDIUM","SLOW"};
 
  //Initializing the array to null
  array = null;
  //Assigning name to the buttons
  sortButton = new JButton("Sort");
  sortButtonRev = new JButton("Sort Reverse");
  stopButton = new JButton("Stop");
  //Disabling the buttons
  sortButton.setEnabled(false);
  sortButtonRev.setEnabled(false);
  //Assigning the name to the Button
  populateButton = new JButton("Populate Array");
  // This ComboBox holds the elements in the elements array
  methodSelector = new JComboBox<String>(elements);
  // This ComboBox holds the elements in the populate_elements array
  populateSelector = new JComboBox<String>(populate_elements);
  // This ComboBox holds the elements in the Speed array
  SpeedSelector = new JComboBox<String>(Speed);
  //instantiating the SortAnimation Panel
  sortDisplay = new SortAnimationPanel();
  //diabling the stop button
  stopButton.setEnabled(false);
  /***
   * this method executes when the populate button is clicked
   * */
  populateButton.addActionListener(new ActionListener()
  {
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
	//This is used to set the Preferred size of the Panel
    Dimension dim = sortDisplay.getPreferredSize();
    //this sets the height of the panel
    int maxValue = dim.height;
    //this sets the width of the panel
    int numOfValues = dim.width;
    //This is used to generate the random numbers
    Random rand = new Random();
    //Assigning the size of the array
    array = new int[numOfValues];
    //This is used to return the string elements inside the orderSelector ComboBox
    String populate_select = String.valueOf(populateSelector.getSelectedItem());
    //This is used to generate random sort  of numbers
    if(populate_select.equals("Random")){
      for(int i=0;i<numOfValues;i++) {
        array[i] = rand.nextInt(maxValue);
      }
    }
    //this is used to sort the elements in Ascending order
    if(populate_select.equals("Ascending")){
      for(int i  = 0; i < numOfValues; i++){
        array[i] = i+1;
      }
    }
    //this is used to sort the elements in Descending order
    if(populate_select.equals("Descending")){
      for(int i  =0; i < numOfValues; i++){
        array[i] = numOfValues-i;
      }
    }
    //As the array is populated we are enabling the sorting button and reverse sorting button
    sortButton.setEnabled(true);
    sortButtonRev.setEnabled(true);
    //This is used to display the values on the SortAnimationPanel
    sortDisplay.repaint();
   }
  });
  
 
  
  //This method executes when the sort button is clicked
  sortButton.addActionListener(new ActionListener() {
   
   @Override
   public void actionPerformed(ActionEvent e) {   
		//assigning the boolean value to true
     ascending = true;
     //This is used to set the sorting method with the help of string values in the methodSelector ComBoBox
     sortDisplay.setSortingMethod(methodSelector.getSelectedIndex());
    //This thread will run the sort on the array
    t = new Thread(sortDisplay);
    //start the thread
    t.start();
    //enabling the stop button
    stopButton.setEnabled(true);
   }
   
  });
  //This method executes when the sort reverse button is clicked
  sortButtonRev.addActionListener(new ActionListener() {
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
	   //assigning the boolean value to false
     ascending = false;
     //This is used to set the sorting method with the help of string values in the methodSelector ComBoBox
     sortDisplay.setSortingMethod(methodSelector.getSelectedIndex());
    //This thread will run the sort on the array
    t = new Thread(sortDisplay);
    //start the thread
    t.start();
    //enabling the stop button
    stopButton.setEnabled(true);
   }
   
  });
  
  stopButton.addActionListener(new ActionListener()
  {
  @Override
   public void actionPerformed(ActionEvent e) 
  {
	  //calling  the doStop method
	  sortDisplay.doStop();
    //disabling the Sort and SortRev button
    populateButton.setEnabled(false);
	sortButton.setEnabled(false);
    sortButtonRev.setEnabled(false);
   }
  });

  //adding the SortAnimationPanel to the JPanel
  add(sortDisplay);
  //adding the populate button to the JPanel
  add(populateSelector);
  add(populateButton);
  //adding the sortButton to the JPanel
  add(sortButton);
  add(sortButtonRev);
  //Adding the sort selector JComboBox to the JPanel
  add(methodSelector);
  //adding the stop Button to the JPanel
  add(stopButton);
  //Adding the speed selector JComboBox to the JPanel
  add(SpeedSelector);
 
  
 }
 /****
  * The SortAnimationPanel class will display the visual results of sorting
  *  and should inherit from JPanel and implement the Runnable interface
  * ********/
 private class SortAnimationPanel extends JPanel implements Runnable
 {
 
  //Constant variable for setting the delay time of thread to the value returned by the JCombobox
  public static final int FAST = 100;
  public static final int MEDIUM = 100;
  public static final int SLOW = 10000;
  
  //Constant associated to the index returned by the JComboBox index
  public static final int INSERTION_SORT = 0;
  public static final int QUICK_SORT = 1;
  public static final int BUBBLE_SORT = 2;
  
  //Sortmethod is used for the constant value of sortingMethod in the panel
  private int sortMethod;
  //boolean for stop method
  private boolean doStop = false;
  //This method is used to set the sorting method for the Panel
  public void setSortingMethod(int sortMethod) throws IllegalArgumentException {
	   //checks if the sortMethod is equal to the value as InsertionSOrt and assigns the value to sortMethod
   if(sortMethod == INSERTION_SORT) 
   {
    this.sortMethod = INSERTION_SORT;
   }
    //checks if the sortMethod is equal to the value as QuickSort and assigns the value to sortMethod
   else if(sortMethod == QUICK_SORT)
   {  
    this.sortMethod = QUICK_SORT;
    
   }
    //checks if the sortMethod is equal to the value as BubbleSort and assigns the value to sortMethod
   else if(sortMethod == BUBBLE_SORT){ //As BubbleSort
    this.sortMethod = BUBBLE_SORT;
   }else
   {  //if the selected method doesn't exist, the throw an esception
    throw new IllegalArgumentException("Method doesn't exist");
   }
  }
  
  //Constructor
  public SortAnimationPanel() {
   //set the background to the JPanel 
   setBackground(Color.WHITE);
   //Set the size of the panel
   setPreferredSize(new Dimension(730,600));
  }
  
  //Override the run method of the runnable interface
  @Override
  public void run() {
    //This variable holds the string value returned from the SpeedSelector ComboBox

   String speedselect = String.valueOf(SpeedSelector.getSelectedItem());
   //varible used to set the speed
   int speed=0 ;
   //checks if the speed is equal to the value as Fast
   if(speedselect.equals("FAST"))
   {
    speed= FAST;
   }
   //checks if the speed is equal to the value as Medium
   else if(speedselect.equals("MEDIUM"))
   {
    speed=MEDIUM;
   }
   //checks if the speed is equal to the value as Slow
   else if(speedselect.equals("SLOW"))
   {
    speed=SLOW;
   }
   //checks if the sortMethod is equal to the value as InsertionSOrt 
   if(sortMethod==INSERTION_SORT)
   {
	   //If the boolean value is true 
     if(ascending)
    	 //calls the method for ascending order
       insertionSort(array,speed);
     else
    	 //calls descending order
       insertionSortRev(array,speed);
   }
   //checks if the sortMethod is equal to the value as QuickSort 
   else if(sortMethod==QUICK_SORT)
   {
		  //If the boolean value is true 
     if(ascending)
    	 //calls the method for ascending order
       quickSort(array,speed);
     else
    	 //calls descending order
       quickSortrev(array,speed);
   }
   //checks if the sortMethod is equal to the value as BubbleSort 
   else if(sortMethod == BUBBLE_SORT)
   {
		  //If the boolean value is true 
     if(ascending)
    	 //calls the method for ascending order
       bubbleSort(array,speed);
     else
    	 //calls descending order
       bubbleSortRev(array,speed);
   }
   //Once the sorting finished, re-enable the populate button
   populateButton.setEnabled(true);
  }

  /****Override the paintComponent to display the array of elements as vertical lines 
   *   for each element of the array, with an height equal to the value of the cell*/
  @Override
  public void paintComponent(Graphics g) {
	//calling the superclass paint component
   super.paintComponent(g);
   //gets the height of the panel
   int height = this.getHeight();
   //Set the color of the graphics to  blue
   g.setColor(Color.BLUE);
   
   //If the array is not null
   if(array != null) {
    //this draws the vertical lines starting from the bottom left corner
    for( int i = 0; i < array.length ; i++ ){
     g.drawLine(i, height, i, height-array[i]);
    }
   }
  }
  
  /** Bubble sort in Ascending Order  with array 
   * and delay time of the thread as parameters
   * */
  public void bubbleSort(int arr[],int THREAD_SLEEP)
  	{
         int n = arr.length;
         for (int i = 0; i < n-1; i++) 
         {
             for (int j = 0; j < n-i-1; j++) 
             {
               if(keepRunning())
               {
                 if (arr[j] > arr[j+1])
                 {
                     // swapping temp and arr[i]

                     int temp = arr[j];
                     arr[j] = arr[j+1];
                     arr[j+1] = temp;
                     
                     //after a swap, repaint the panel so we can show the panel
                  repaint();
                  //pause the thread to slow down the sorting
                  try 
                  {
                    Thread.sleep(THREAD_SLEEP);
                  } catch (InterruptedException e) 
                  {
                    e.printStackTrace();
                  }
                }
              }
               else
                 return;
          }
         }       
     }
  /** Bubble sort in Reverse Order  with array 
   * and delay time of the thread as parameters
   * */
  public void bubbleSortRev(int arr[],int THREAD_SLEEP)
  {
         int n = arr.length;
         for (int i = 0; i < n-1; i++) 
         {
             for (int j = 0; j < n-i-1; j++)
             {
               if(keepRunning())
               {
                 if (arr[j] < arr[j+1])
                 {
                     // swap temp and arr[i]

                     int temp = arr[j];
                     arr[j] = arr[j+1];
                     arr[j+1] = temp;
                     
                     //after a swap, repaint the panel so we can show the animation
                  repaint();
                  //pause the thread to slow down the animation
                  try 
                  {
                    Thread.sleep(THREAD_SLEEP);
                  } catch (InterruptedException e)
                  {
                    e.printStackTrace();
                  }
             }
               }
               else
                 return;
             }
          }    
     }
  
  /** Insertion sort in Ascending Order  with array 
   * and delay time of the thread as parameters
   * */
  public void insertionSort(int arr[],int THREAD_SLEEP){
         int n = arr.length;
         for (int i=1; i<n; ++i){
             int key = arr[i];
             int j = i-1;
  
             /* Move elements of arr[0..i-1], that are
                greater than key, to one position ahead
                of their current position */
             while (j>=0 && arr[j] > key){
               if(keepRunning())
               {
                 arr[j+1] = arr[j];
                 j = j-1;
                 
                 //after a swap, repaint the panel so we can show the animation
                 repaint();
                 //pause the thread to slow down the animation
                 try {
                   Thread.sleep(THREAD_SLEEP);
                 } catch (InterruptedException e) {
                   e.printStackTrace();
                 }
               }
               else{
                 return;
               }
            }
             arr[j+1] = key;
             
             
         }
     }
  /** Insertion sort in Reverse Order  with array 
   * and delay time of the thread as parameters
   * */
  public void insertionSortRev(int arr[],int THREAD_SLEEP)
  {
         int n = arr.length;
         for (int i=1; i<n; ++i)
         {
             int key = arr[i];
             int j = i-1;
  
             /* Move elements of arr[0..i-1], that are
                greater than key, to one position ahead
                of their current position */
             while (j>=0 && arr[j] < key)
             {
               if(keepRunning())
               {
                 arr[j+1] = arr[j];
                 j = j-1;
                 
                 //after a swap, repaint the panel so we can show the panel
                 repaint();
                 //pause the thread to slow down the sorting
                 try 
                 {
                   Thread.sleep(THREAD_SLEEP);
                 } catch (InterruptedException e)
                 {
                   e.printStackTrace();
                 }
               }
               else
               {
                return; 
               }
              }
             arr[j+1] = key;            
         }
     }
  
  /** Quicksort in Ascending order  with array 
   * and delay time of the thread as parameters
   * */ 
     public void quickSort(int arr[],int THREAD_SLEEP)
     {
      //calculate the array length
      int n = arr.length;
      //call quicksort
      quickSort(arr, 0, n-1);
     }
     
  /* This function takes last element as pivot,
     places the pivot element at its correct
     position in sorted array, and places all
     smaller (smaller than pivot) to left of
     pivot and all greater elements to right
     of pivot */
  private int partition(int arr[], int low, int high)
  {
       int pivot = arr[high]; 
       int i = (low-1); // index of smaller element
       
       for (int j=low; j<high; j++)
       {
        // If current element is smaller than or
           // equal to pivot
         if(keepRunning())
         {
           if (arr[j] <= pivot)
           {
            i++;
  
               // swap arr[i] and arr[j]
               int temp = arr[i];
               arr[i] = arr[j];
               arr[j] = temp;
               
               //after a swap, repaint the panel so we can show on the panel
               repaint();
               //pause the thread to slow down the sorting
               try 
               {
                Thread.sleep(THREAD_SLEEP);
               } catch (InterruptedException e) 
               {
            	   e.printStackTrace();
               }
           }
         }
         else
           return -1;
       }
  
       // swap arr[i+1] and arr[high] (or pivot)
       
         int temp = arr[i+1];
         arr[i+1] = arr[high];
         arr[high] = temp;
         repaint();
             //pause the thread to slow down the animation
             try 
             {
              Thread.sleep(THREAD_SLEEP);
             } catch (InterruptedException e) 
             {
               e.printStackTrace();
             }
       
      
       return i+1;
   }
     
  /** The main function that implements QuickSort has Array to be sorted,
  Starting index,Ending index as parameters **/
   private  void quickSort(int arr[], int low, int high)
   {
       if (low < high)
       {
           /* pi is partitioning index, arr[pi] is 
             now at right place */
           int pi = partition(arr, low, high);
           if(pi == -1)
             return;
           // Recursively sort elements before
           // partition and after partition
           quickSort(arr, low, pi-1);
           quickSort(arr, pi+1, high);
       }
   }
   /** Quicksort in Reverse Order  with array 
    * and delay time of the thread as parameters
    * */
   public void quickSortrev(int arr[],int THREAD_SLEEP)
   	{
       //calculate the array length
       int n = arr.length;
       //call quicksort
       quickSortReverse(arr, 0, n-1);
    }
   /** This function takes last element as pivot,
   places the pivot element at its correct
   position in sorted array, and places all
   larger  to left of  pivot and all smaller elements to right
   of pivot */
   private int partitionRev(int arr[], int low, int high)
   {
       int pivot = arr[high]; 
       int i = (low-1); // index of smaller element
       
       for (int j=low; j<high; j++)
       {
        // If current element is smaller than or
           // equal to pivot
         if(keepRunning())
         {
           if (arr[j] > pivot)
           {
            i++;
  
               // swap arr[i] and arr[j]
               int temp = arr[i];
               arr[i] = arr[j];
               arr[j] = temp;
               
               //after a swap, repaint the panel so we can show the panel
               repaint();
               //pause the thread to slow down the sorting
               try 
               {
                Thread.sleep(THREAD_SLEEP);
               } catch (InterruptedException e) 
               {
            	   e.printStackTrace();
               }
           }
         }
         else
           return -1;
       }
  
       // swap arr[i+1] and arr[high] (or pivot)
       
         int temp = arr[i+1];
         arr[i+1] = arr[high];
         arr[high] = temp;
       
         repaint();
             //pause the thread to slow down the animation
             try
             {
              Thread.sleep(THREAD_SLEEP);
             } catch (InterruptedException e) 
             {
               e.printStackTrace();
             }
       
       return i+1;
   }
  
  
   
   private  void quickSortReverse(int arr[], int low, int high)
   {
       if (low < high)
       {
           /* pi is partitioning index, arr[pi] is 
             now at right place */
           int pi = partitionRev(arr, low, high);
           if(pi == -1)
             return;
           // Recursively sort elements before
           // partition and after partition
           quickSortReverse(arr, low, pi-1);
           quickSortReverse(arr, pi+1, high);
       }
   }
   /*Synchronised method to stop the sort*/
   public synchronized void doStop()
   {
     this.doStop = true;
   }
      /*Returns a boolean value*/
   public synchronized boolean keepRunning() 
   {
     if(this.doStop)
       {
    	 return false;
       }
     return true;
     
   }
 }
}



