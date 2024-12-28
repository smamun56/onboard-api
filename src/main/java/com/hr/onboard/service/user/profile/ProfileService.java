package com.hr.onboard.service.user.profile;

import com.hr.onboard.data.PageList;
import com.hr.onboard.data.user.UserProfile;
import com.hr.onboard.exception.UserDoesNotExist;

import java.util.List;

public interface ProfileService {
    /**
     * load UserProfile with given userId
     *
     * @param userId
     * @return
     * @throws UserDoesNotExist if target user is not exist
     */
    UserProfile getProfile(String userId) throws UserDoesNotExist;

    /**
     * get all user profiles
     *
     * @return all user profiles
     */
    List<UserProfile> getAllUserProfiles();

    /**
     * get all user profiles with page request
     *
     * @param page must>=0
     * @param size must>0
     * @return paged user profiles
     */
    PageList<UserProfile> getAllUserProfilesWithPage(int page, int size);
}
