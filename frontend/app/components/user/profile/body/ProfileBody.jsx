import { API_URL } from '@env'
import { useEffect, useState } from 'react';
import { View } from 'react-native';
import { ProfileField } from './ProfileField';
import tw from 'tailwind-react-native-classnames';

export const ProfileBody = ({ discordID }) => {

    const [information, setInformation] = useState(null);

    useEffect(() => {
        let json = retrieveUserInformation(discordID);
        if (json !== null)
            setInformation(json.userInformation);
    });

    return (
        <View style={tw`flex-1 bg-gray-200 rounded-t-3xl p-4 mx-5 shadow-xl`}>
            <View style={tw`mx-5`}>
                <ProfileField icon={require("../../../../../assets/images/icons/bodyIcon.png")} text={"97.8 KG"} />
                <ProfileField icon={require("../../../../../assets/images/icons/fatIcon.png")} text={"27.0 Kg"} />
                <ProfileField icon={require("../../../../../assets/images/icons/muscleIcon.png")} text={"66.3 Kg"} />
                <ProfileField icon={require("../../../../../assets/images/icons/waterIcon.png")} text={"50.5 Kg"} />
            </View>
        </View>
    )
}

async function retrieveUserInformation(discordID) {
    let response = await fetch(API_URL + "/users/user/" + discordID, {
        method: "GET",
        mode: 'cors',
        headers: {
            'Authorization': "" //TODO Auth Code
        }
    });

    if (response.status !== 200)
        return null;

    return await response.json();
}