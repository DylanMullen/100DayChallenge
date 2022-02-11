import { View, Text } from 'react-native'
import React from 'react'
import tw from 'tailwind-react-native-classnames'
import { SafeAreaView } from 'react-native-safe-area-context'
import { Profile } from '../../../components/user/profile/Profile'

const UserProfilePage = () => {
  return (
    <SafeAreaView style={tw`flex-1`}>
      <Profile />
    </SafeAreaView>
  )
}

export default UserProfilePage