import { useEffect, useState } from "react"
import { DISCORD_API, DISCORD_CDN } from '@env'
import { View, Image, Text } from "react-native";
import tw from "tailwind-react-native-classnames";

const DEFAULT_AVATAR = "https://media.istockphoto.com/vectors/default-profile-picture-avatar-photo-placeholder-vector-illustration-vector-id1223671392?k=20&m=1223671392&s=612x612&w=0&h=lGpj2vWAI3WUT1JeJWm1PRoHT3V15_1pdcTn2szdwQ0=";

export const ProfileHeader = ({ discordID }) => {

    const [information, setInformation] = useState(null);
    useEffect(() => {
        let information = retrieveDiscordInformation(discordID);
        if (information == null)
            return;

        setInformation({ username: "TwixDylan", avatar: "https://media.istockphoto.com/vectors/default-profile-picture-avatar-photo-placeholder-vector-illustration-vector-id1223671392?k=20&m=1223671392&s=612x612&w=0&h=lGpj2vWAI3WUT1JeJWm1PRoHT3V15_1pdcTn2szdwQ0=" });
    });

    return (
        <View style={tw`flex-1 justify-center items-center`}>
            <View>
                <Image style={tw`w-52 h-52 rounded-full`} source={
                    { uri: (information ? information.avatar : DEFAULT_AVATAR) }
                } />
            </View>
            <Text style={tw`absolute bottom-7 text-3xl tracking-wide `}>{information !== null ? information.username : "Failed to load Username"}</Text>
        </View>
    )
}

async function retrieveDiscordInformation(discordID) {
    let response = await fetch(DISCORD_API + "/users/" + discordID, {
        method: "GET",
        mode: 'cors',
        headers: {
            'Authorization': "Bearer " //TODO Discord Code
        }
    });

    if (response !== 200)
        return null;

    let json = await response.json();

    return {
        username: json.username,
        avatar: DISCORD_CDN + "/avatars/" + discordID + "/" + json.avatar + ".png"
    };
}