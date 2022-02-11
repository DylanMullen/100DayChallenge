import { View, Text } from 'react-native'
import React from 'react'
import { createNavigationContainerRef } from '@react-navigation/core'
import { createNativeStackNavigator } from '@react-navigation/native-stack'

import LoginPage from './login';
import UserProfilePage from './user/profile';

const Stack = createNativeStackNavigator();

const NavigationStack = () => {
  return (
    <Stack.Navigator screenOptions={{headerShown: false}}>
      <Stack.Screen name="profile" component={UserProfilePage} />
      {/* <Stack.Screen name="login" component={LoginPage} /> */}
    </Stack.Navigator>
  )
}

export default NavigationStack