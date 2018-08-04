package rmc_core_2018_2019.framework;

import clojure.lang.IPersistentMap;

import java.util.List;

/*****************************************************************************
 * Components represent concurrent and independent programs within the robot
 * application.
 *
 * The life cycle of a component has two phases: init and run.
 *
 * During the init phase, the component initializes it's state. The init
 * phase is called once when application is launched. The application may be
 * launched multiple times over the course of a match, particularly if power
 * failures result in abrupt power cuts to the robot.
 *
 * During the run phase, the component's runComponent method is repeatedly
 * called by a scheduler. If the robot is currently in a 'dead' state then
 * the runComponent method will not be called.
 *
 * Components can request access to maps containing elements of the robots
 * global state using the getInitFields and getRunFields methods which
 * specify the globals requested.
 *
 * @author Noah Kennedy
 *****************************************************************************/
public interface IComponent {
    
    /*************************************************************************
     * Initializes the component. This method is called once when the
     * application is launched. Any setup code that requires access to the
     * global state of the program should be placed in here.
     *
     * @param inputMap An IPersistentMap containing any key/value pairings
     *                 that the component requested. The supplied keys are
     *                 the field enums which the component requested and the
     *                 supplied values are the objects at those fields. All
     *                 inputs should be checked at runtime using assertions.
     *
     * @return An IPersistentMap containing any key/value pairings that this
     * function should alter referentially. A field is altered referentially if
     * the reference of the value object contained in the key/value pair is
     * altered. If the underlying object is altered by performing an action
     * which alters the state of the object using methods of the object then no
     * referential changes have been made.
     *************************************************************************/
    IPersistentMap initializeComponent (IPersistentMap inputMap);
    
    /*************************************************************************
     * Runs the component. This method is called repeatedly after the
     * component has been initialized.
     *
     * @param inputMap An IPersistentMap containing any key/value pairings that
     *                 the component requested. The supplied keys are the field
     *                 enums which the component requested and the supplied
     *                 values are the objects at those fields. All inputs should
     *                 be checked at runtime using assertions.
     *
     * @return An IPersistentMap containing any key/value pairings that this
     * function should alter referentially. A field is altered referentially if
     * the reference of the value object contained in the key/value pair is
     * altered. If the underlying object is altered by performing an action
     * which alters the state of the object using methods of the object then no
     * referential changes have been made.
     *************************************************************************/
    IPersistentMap runComponent (IPersistentMap inputMap);
    
    /*************************************************************************
     * Queries the component for the fields required to initialize the
     * component. These fields will later be passed into the initializeComponent
     * method by the framework.
     *
     * @return Returns a List of field enums containing the fields required
     * by the initializeComponent method.
     *************************************************************************/
    List <Fields> getInitFields ();
    
    /*************************************************************************
     * Queries the component for the fields required to run the
     * component. These fields will later be passed into the runComponent
     * method by the framework.
     *
     * @return Returns a List of field enums containing the fields required
     * by the runComponent method.
     *************************************************************************/
    List <Fields> getRunFields ();
}
