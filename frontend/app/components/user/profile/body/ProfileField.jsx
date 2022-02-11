import { Image, StyleSheet, Text, View } from "react-native"
import tw from "tailwind-react-native-classnames"

export const ProfileField = ({ icon, text }) => {

    return (
        <View style={tw`flex-row my-2 h-20`}>
            <View style={[tw`w-20 rounded-full z-10 bg-red-500 justify-center items-center`, { aspectRatio: 1 }]}>
                <Image  style={[tw`w-2/3 h-2/3`]} source={icon} />
            </View>
            <View style={tw`flex-1 rounded-r-full bg-red-200 my-4 -ml-3 z-0 justify-center items-center`}>
                <Text>{text}</Text>
            </View>
        </View>
    )
}
