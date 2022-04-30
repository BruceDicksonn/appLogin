package factory;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class FactoryFirebaseInstances {

    public FirebaseAuth createFireBaseAuthInstance(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth;
    }

    public FirebaseApp createFireBaseAppInstance(){
        FirebaseApp app = FirebaseApp.getInstance();
        return app;
    }


}
