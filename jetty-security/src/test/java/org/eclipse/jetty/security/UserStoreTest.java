//
//  ========================================================================
//  Copyright (c) 1995-2017 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.security;

import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserStoreTest
{
    UserStore userStore;

    @Before
    public void setup() {
        userStore = new UserStore();
    }

    @Test
    public void addUser()
    {
        this.userStore.addUser( "foo", Credential.getCredential( "beer" ), new String[]{"pub"} );
        Assert.assertEquals(1, this.userStore.getKnownUserIdentities().size());
        UserIdentity userIdentity = this.userStore.getUserIdentity( "foo" );
        Assert.assertNotNull( userIdentity );
        Assert.assertEquals( "foo", userIdentity.getUserPrincipal().getName() );
        Set<AbstractLoginService.RolePrincipal>
            roles = userIdentity.getSubject().getPrincipals( AbstractLoginService.RolePrincipal.class);
        List<String> list = roles.stream()
            .map( rolePrincipal -> rolePrincipal.getName() )
            .collect( Collectors.toList() );
        Assert.assertEquals(1, list.size());
        Assert.assertEquals( "pub", list.get( 0 ) );
    }

    @Test
    public void removeUser()
    {
        this.userStore.addUser( "foo", Credential.getCredential( "beer" ), new String[]{"pub"} );
        Assert.assertEquals(1, this.userStore.getKnownUserIdentities().size());
        UserIdentity userIdentity = this.userStore.getUserIdentity( "foo" );
        Assert.assertNotNull( userIdentity );
        Assert.assertEquals( "foo", userIdentity.getUserPrincipal().getName() );
        userStore.removeUser( "foo" );
        userIdentity = this.userStore.getUserIdentity( "foo" );
        Assert.assertNull( userIdentity );
    }

}
