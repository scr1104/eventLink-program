package use_case;

import entity.Vip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VipManager implements Serializable {

    /**
     * Represents the UserManager that manages this Vip
     */
    UserManager userManager;
    public List<String> vipIdList;
    public List<String> vipCodeList;

    /**
     * Represents the VipManager that manages this Vip.
     */
    public VipManager(UserManager um) {
        userManager = um;
        vipIdList = new ArrayList<>();
        vipCodeList = new ArrayList<>();
    }

    /**
     * Creates a new Vip with the specified name, id, password, and adds new Vip to userList.
     * @param name The name of this Vip.
     * @param id The id of this Vip.
     * @param pw The password of this Vip.
     */

    public boolean newVip(String name, String id, String pw, String code) {
        Vip vip = new Vip(name, id, pw, code);
        userManager.userList.add(vip);
        vipIdList.remove(id);
        vipCodeList.remove(code);
        return true;
    }
    /**
     * Creates a new Vip with the specified name, id, password, and adds new Vip to userList.
     * @param name The name of this Vip.
     * @param id The id of this Vip.
     * @param pw The password of this Vip.
     * For organizerController purpose only.
     */

    public boolean newVip(String name, String id, String pw) {
        String code;
        if (this.vipCodeList.isEmpty()) {
            code = "a";
        } else {
            code = this.vipCodeList.get(0) + "a";
        }
        Vip vip = new Vip(name, id, pw, code);
        userManager.userList.add(vip);
        vipIdList.remove(id);
        vipCodeList.remove(code);
        return true;
    }

    /**
     * Checks to see if Vip is enrolled in specified event.
     * @param event The name of the event.
     */
    public boolean isEnrolled(String event) {
        Vip vip = (Vip) userManager.userIded(userManager.getCurrentUserId());
        return vip.isInAttendingEvents(event);
    }

    public boolean isValidCode(String id, String code){
        if (!(vipIdList.contains(id) && vipCodeList.contains(code))){
            return false;}
        String validCode = vipCodeList.get(vipIdList.indexOf(id));
        return validCode.equals(code);
    }

    /**
     * Adds a new exclusive sign-up code.
     * @param id The id that is to be used by the new VIP user signing up with this new code.
     * @param code The code that will be used when the new VIP user signs up.
     */
    public boolean addNewCode(String id, String code){
        if (vipIdList.contains(id) || vipCodeList.contains(code)){
            return false;
        }
        vipIdList.add(id);
        vipCodeList.add(code);
        return true;
    }

    /**
     * Serialize all vips id.
     */
    public void serialize() {
        try {
            FileOutputStream fos = new FileOutputStream("ser_files/vipList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(vipIdList);
            oos.close();
            fos.close();

            FileOutputStream fos2 = new FileOutputStream("ser_files/vipCode.ser");
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(vipCodeList);
            oos2.close();
            fos2.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }



    /**
     * Deserialize all users from "vipList.ser" and "vipCode.ser" files.
     */
    public void deserialize(){

        List<String> vips = new ArrayList<>();
        List<String> vipCode = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream("ser_files/vipList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);

            vips = (List<String>) ois.readObject();

            ois.close();
            fis.close();

            FileInputStream fis2 = new FileInputStream("ser_files/vipCode.ser");
            ObjectInputStream ois2 = new ObjectInputStream(fis2);

            vipCode = (List<String>) ois2.readObject();

            ois2.close();
            fis2.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
        }

        for (String code: vipCode) {
            System.out.println(code);
        }

        vipIdList = vips;
        vipCodeList = vipCode;
    }



}
