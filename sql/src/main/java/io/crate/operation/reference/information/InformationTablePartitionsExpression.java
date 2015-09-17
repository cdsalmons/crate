/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */
package io.crate.operation.reference.information;

import io.crate.metadata.PartitionInfo;
import io.crate.metadata.RowContextCollectorExpression;
import io.crate.metadata.Schemas;
import org.apache.lucene.util.BytesRef;

import java.util.Map;

public abstract class InformationTablePartitionsExpression<T>
        extends RowContextCollectorExpression<PartitionInfo, T> {

    public static class PartitionsTableNameExpression extends InformationTablePartitionsExpression<BytesRef> {
        @Override
        public BytesRef copyValue() {
            return new BytesRef(row.name().tableName());
        }
    }

    public static class PartitionsSchemaNameExpression extends InformationTablePartitionsExpression<BytesRef> {

        private final BytesRef DOC_SCHEMA_INFO = new BytesRef(Schemas.DEFAULT_SCHEMA_NAME);

        @Override
        public BytesRef copyValue() {
            String schemaName = row.name().schemaOrNull();
            if (schemaName == null) {
                return DOC_SCHEMA_INFO;
            }
            return new BytesRef(schemaName);
        }
    }

    public static class PartitionsPartitionIdentExpression extends InformationTablePartitionsExpression<BytesRef> {
        @Override
        public BytesRef copyValue() {
            return new BytesRef(row.name().ident());
        }
    }
    public static class PartitionsValuesExpression extends InformationTablePartitionsExpression<Map<String, Object>> {
        @Override
        public Map<String, Object> copyValue() {
            return row.values();
        }
    }
    public static class PartitionsNumberOfShardsExpression extends InformationTablePartitionsExpression<Integer> {

        @Override
        public Integer copyValue() {
            return row.numberOfShards();
        }
    }

    public static class PartitionsNumberOfReplicasExpression extends InformationTablePartitionsExpression<BytesRef> {
        @Override
        public BytesRef copyValue() {
            return row.numberOfReplicas();
        }
    }
}
