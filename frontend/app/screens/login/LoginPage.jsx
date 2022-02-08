
import { View } from "react-native"

import DiscordButton from "../../components/buttons/discord"


export const LoginPage = () =>
{
    
    return (
        <View>
            <DiscordButton onPress={() => console.log("test")} />
        </View>
    )

}