/********************************************************************************
 * Copyright (c) 2021-2023 Robert Bosch Manufacturing Solutions GmbH
 * Copyright (c) 2021-2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/
package org.eclipse.tractusx.semantics.registry.utils;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShellSpecification<T> implements Specification<T> {

   private final String sortFieldName;
   private final ShellCursor shellCursor;

   @Override
   public Predicate toPredicate( Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder criteriaBuilder ) {
      return applyFilter( root,cq, criteriaBuilder );
   }

   private Predicate applyFilter( Root<T> root,CriteriaQuery<?> cq, CriteriaBuilder criteriaBuilder ) {
      if(root.toString().contains( "Shell" )){
         var searchValue = shellCursor.getShellSearchCursor();
         cq.orderBy( criteriaBuilder.asc( criteriaBuilder.coalesce( root.get( sortFieldName ), Instant.now() ) ) );
         return criteriaBuilder.greaterThan( root.get( sortFieldName ), searchValue );
      }else{
         var searchValue = shellCursor.getSubmodelSearchCursor();
         cq.orderBy( criteriaBuilder.asc( criteriaBuilder.coalesce( root.get( sortFieldName ),
               UUID.fromString( "00000000-0000-0000-0000-000000000000" )) ) );
         return criteriaBuilder.greaterThan( root.get( sortFieldName ), searchValue );
      }
   }
}