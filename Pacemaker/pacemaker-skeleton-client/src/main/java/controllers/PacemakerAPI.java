package controllers;

import java.util.Collection;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Activity;
import models.Location;
import models.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface PacemakerInterface {
  @GET("/users")
  Call<List<User>> getUsers();
  
  @POST("/users")
  Call<User> registerUser(@Body User User);

  @GET("/users/{id}/activities")
  Call<List<Activity>> getActivities(@Path("id") String id);

  @POST("/users/{id}/activities")
  Call<Activity> addActivity(@Path("id") String id, @Body Activity activity);

  @GET("/users/{id}/activities/{activityId}")
  Call<Activity> getActivity(@Path("id") String id, @Path("activityId") String activityId);  
  @GET("/users/{id}/activities/{activityId}/locations")
  Call<List<Location>> getActivityLocations(@Path("id") String id, @Path("activityId") String activityId);

  @POST("/users/{id}/activities/{activityId}/locations")
  Call<Location> addLocation(@Path("id") String id, @Path("activityId") String activityId, @Body Location location);
  
  @GET("/users/{id}/friends/{email}")
  Call<String> followfriend(@Path("id") String id, @Path("email") String emailid);   
  
  @GET("/users/{id}/friendslist")
  Call<List<User>> getFriends(@Path("id") String id);
  
  @POST("/users/{id}/message/{email}")
  Call<String> messageFriend(@Path("id") String id, @Path("email") String emailid, @Body String message);   
  
  @GET("/users/{id}/retrivemessage")
  Call<List<String>> getMessages(@Path("id") String id);
  
  @POST("/users/{id}/friends/{email}")
  Call<String> unfollowfriend(@Path("id") String id, @Path("email") String emailid);
}


public class PacemakerAPI {

  PacemakerInterface pacemakerInterface;

  public PacemakerAPI(String url) {
    Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson)).build(); 
    
    pacemakerInterface = retrofit.create(PacemakerInterface.class);
  }

  public Collection<User> getUsers() {
    Collection<User> users = null;
    try {
      Call<List<User>> call = pacemakerInterface.getUsers();
      Response<List<User>> response = call.execute();
      users = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

  public void deleteUsers() {}

  public User createUser(String firstName, String lastName, String email, String password) {
    User returnedUser = null;
    try {
      Call<User> call = pacemakerInterface.registerUser(new User(firstName, lastName, email, password));
      Response<User> response = call.execute();
      returnedUser = response.body();    
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return returnedUser;
  }

  public Activity createActivity(String id, String type, String location, double distance) {
    Activity returnedActivity = null;
    try {
      Call<Activity> call = pacemakerInterface.addActivity(id, new Activity(type, location, distance));
      Response<Activity> response = call.execute();
      returnedActivity = response.body();    
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return returnedActivity;
  }


  public Collection<Activity> getActivities(String id) {
    Collection<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.getActivities(id);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }

  public List<Activity> listActivities(String userId, String sortBy) {
	  List<Activity> activities = null;
	    try {
	      Call<List<Activity>> call = pacemakerInterface.getActivities(userId);
	      Response<List<Activity>> response = call.execute();
	      activities = response.body();
	      
	      switch (sortBy) {
	      case "type":
	        activities.sort((a1, a2) -> a1.type.compareTo(a2.type));
	        break;
	      case "location":
	        activities.sort((a1, a2) -> a1.location.compareTo(a2.location));
	        break;
	      case "distance":
	        activities.sort((a1, a2) -> Double.compare(a1.distance, a2.distance));
	        break;
	    }
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return activities;
  }

  public void addLocation(String id, String activityId, double latitude, double longitude) {
	    try {
	      Call<Location> call = pacemakerInterface.addLocation(id, activityId, new Location(latitude, longitude));
	      call.execute();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	  }

  public User getUserByEmail(String email) {
    Collection<User> users = getUsers();
    User foundUser = null;
    for (User user : users) {
      if (user.email.equals(email)) {
        foundUser = user;
      }
    }
    return foundUser;
  }
  public Collection<Location> getActivityLocations(String id, String activityId) {
	  
	  Collection<Location> locations = null;
    try {
      Call<List<Location>> call = pacemakerInterface.getActivityLocations(id, activityId);
      Response<List<Location>> response = call.execute();
      locations = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return locations;
  }

  public Activity getActivity(String userId, String activityId) {
	  Activity activity = null;
	    try {
	      Call<Activity> call = pacemakerInterface.getActivity(userId, activityId);
	      Response<Activity> response = call.execute();
	      activity = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return activity;
  }

  public String follow(String userId, String email) {
	  String te = null;
	    try {
	      Call<String> call = pacemakerInterface.followfriend(userId, email);
	      Response<String> response = call.execute();
	      te = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return te;
  }
  
  public String unfollow(String userId, String email) {
	  String te = null;
	    try {
	      Call<String> call = pacemakerInterface.unfollowfriend(userId, email);
	      Response<String> response = call.execute();
	      te = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return te;
  }
  public User deleteUser(String id) {
	    return null;
	  }
  public Collection<User> getfriends(String id) {
	    Collection<User> friends = null;
	    try {
	      Call<List<User>> call = pacemakerInterface.getFriends(id);
	      Response<List<User>> response = call.execute();
	      friends = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return friends;
	  }

  public String messageFriend(String userId, String email, String text) {
	  String te = null;
	  
	    try {
	    	System.out.println("INSIDE the api");
	      Call<String> call = pacemakerInterface.messageFriend(userId, email, text);
	      Response<String> response = call.execute();
	      te = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return te;
  }
  
  public Collection<String> getmessages(String id) {
	    Collection<String> friends = null;
	    try {
	      Call<List<String>> call = pacemakerInterface.getMessages(id);
	      Response<List<String>> response = call.execute();
	      friends = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return friends;
	  }
  
 
 
}
